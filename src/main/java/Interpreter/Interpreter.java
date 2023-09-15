package Interpreter;

import Parser.AST.Expressions.*;
import Parser.AST.Statements.*;
import Parser.AST.*;
import ObjSystem.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Interpreter {

    public static Null NULL = new Null();
    static Bool TRUE = new Bool(true);
    static Bool FALSE = new Bool(false);

    public static Entity eval(Node pNode, Environment env) {
        // Whole program
        if (pNode.getClass().equals(Program.class))
            return evalProgram(((Program) pNode).getStatements(), env);
        // Expression statements
        else if (pNode.getClass().equals(ExpressionStatement.class))
            return eval(((ExpressionStatement) pNode).value(), env);
        // Block statements
        else if (pNode.getClass() == BlockStatement.class)
            return evalBlockStatement((BlockStatement) pNode, env);
        // Return statements
        else if (pNode.getClass() == ReturnStatement.class) {
            Entity val = eval(((ReturnStatement) pNode).value(), env);
            if (isError(val))
                return val;
            return new ReturnValue(val);
        }
        // Integer Literals
        else if (pNode.getClass().equals(IntegerLiteral.class))
            return new Int(((IntegerLiteral) pNode).value());
        // Boolean Literals
        else if (pNode.getClass().equals(BooleanLiteral.class))
            return getBoolObject(((BooleanLiteral) pNode).value());
        // String Literals
        else if (pNode.getClass()== StringLiteral.class)
            return new Str(((StringLiteral) pNode).value());
        // Prefix Expressions
        else if (pNode.getClass() == PrefixExpression.class) {
            Entity right = eval(((PrefixExpression) pNode).right(), env);
            if (isError(right))
                return right;
            return evalPrefixExpression(((PrefixExpression) pNode).op(), right);
        }
        // Infix Expressions
        else if (pNode.getClass() == InfixExpression.class) {
            Entity left = eval(((InfixExpression) pNode).left(), env);
            if (isError(left))
                return left;
            Entity right = eval(((InfixExpression) pNode).right(), env);
            if (isError(right))
                return right;
            assert left != null;
            return evalInfixExpression(((InfixExpression) pNode).op(), left, right);
        }
        // If Expressions
        else if (pNode.getClass() == IfExpression.class) {
            Entity condition = eval((((IfExpression) pNode).condition()), env);
            if (isError(condition))
                return condition;
            return evalIfExpression(condition, ((IfExpression) pNode).consequence(), ((IfExpression) pNode).alternative(), env);
        }
        // Let Statements
        else if (pNode.getClass() == LetStatement.class) {
            Entity val = eval(((LetStatement) pNode).value(), env);
            if (isError(val))
                return val;
            env.set(((LetStatement) pNode).name().value(), val);
        }
        // Identifiers
        else if (pNode.getClass() == Identifier.class)
            return evalIdentifier(pNode, env);
        // Function Literals
        else if (pNode.getClass() == FunctionLiteral.class){
            List<Identifier> params = ((FunctionLiteral) pNode).parameters();
            BlockStatement body = ((FunctionLiteral) pNode).body();
            return new Function(params, body, env);
        }
        // Call Expressions
        else if (pNode.getClass() == CallExpression.class) {
            Entity func = eval(((CallExpression) pNode).function(), env);
            if (isError(func))
                return func;
            List<Entity> args = evalArguments(((CallExpression) pNode).params(), env);
            if (args.size() == 1 && isError(args.get(0)))
                return args.get(0);
            return evalFunction(func, args);
        }
        // default
        return NULL;
    }

    private static Entity evalFunction(Entity func, List<Entity> args) {
        if (func.getClass() != Function.class)
            return newError("not a function: %s", func.Type());
        Environment extendedEnv = extendedFunctionEnv(func, args);
        Entity evalBody = eval(((Function) func).body(), extendedEnv);
        assert evalBody != null;
        return unwrapReturnVal(evalBody);
    }

    private static Entity unwrapReturnVal(Entity obj) {
        if (obj.getClass() == ReturnValue.class)
            return ((ReturnValue) obj).value();
        return obj;
    }

    private static Environment extendedFunctionEnv(Entity func, List<Entity> args) {
        assert func.getClass() == Function.class;
        EnclosedEnvironment newEnv = new EnclosedEnvironment(((Function) func).env());
        int i = 0;
        for (Identifier name: ((Function) func).parameters()) {
            newEnv.set(name.value(), args.get(i));
            i++;
        }
        return newEnv;
    }

    private static List<Entity> evalArguments(List<Expression> params, Environment env) {
        List<Entity> arguments = new ArrayList<>(0);
        for (Expression arg: params) {
            Entity result = eval(arg, env);
            if (isError(result)){
                return new ArrayList<>(List.of(result));
            }
            arguments.add(result);
        }
        return arguments;
    }

    private static Entity evalIdentifier(Node pNode, Environment env) {
        Entity result = env.get(((Identifier) pNode).value());
        if (result != NULL)
            return result;
        return newError("Identifier not found: %s", ((Identifier) pNode).value());
    }

    private static Entity evalBlockStatement(BlockStatement pBlock, Environment env) {
        Entity result = null;
        for (Statement stmt: pBlock.statements()) {
            result = eval(stmt, env);
            if (result != null) {
                if (result.Type() == EntityType.RETURN_VALUE_OBJ || result.Type() == EntityType.ERROR_OBJ)
                    return result;
            }
        }
        return result;
    }

    private static Entity evalIfExpression(Entity condition, BlockStatement consequence, BlockStatement alternative, Environment env) {
        if (isTruthy(condition))
            return eval(consequence, env);
        else if (alternative != null)
            return eval(alternative, env);
        else return NULL;
    }

    private static boolean isTruthy(Entity obj) {
        if (obj == TRUE)
            return true;
        return obj != FALSE && obj != NULL;
    }

    private static Entity evalInfixExpression(String op, Entity left, Entity right) {
        if (left.Type() == EntityType.INT_OBJ && right.Type() == EntityType.INT_OBJ)
            return parseIntegerInfixExpression(op, left, right);
        else if (Objects.equals(op, "!="))
            return getBoolObject(left != right);
        else if (Objects.equals(op, "=="))
            return getBoolObject(left == right);
        else if (left.Type() != right.Type())
            return newError("type mismatch: %s %s %s", left.Type(), op, right.Type());
        else
            return newError("unknown operator: %s %s %s", left.Type(), op, right.Type());
    }

    private static Entity parseIntegerInfixExpression(String op, Entity left, Entity right) {
        long leftVal = ((Int) left).value();
        long rightVal = ((Int) right).value();
        switch (op) {
            case "+" -> {return new Int(leftVal + rightVal);}
            case "-" -> {return new Int(leftVal - rightVal);}
            case "*" -> {return new Int(leftVal * rightVal);}
            case "/" -> {return new Int(leftVal / rightVal);}
            case "<" -> {return getBoolObject(leftVal < rightVal);}
            case ">" -> {return getBoolObject(leftVal > rightVal);}
            case "!=" -> {return getBoolObject(leftVal != rightVal);}
            case "==" -> {return getBoolObject(leftVal == rightVal);}
            default -> { return newError("unknown operator: %s %s %s", left.Type(), op, right.Type()); }
        }
    }

    private static Entity evalPrefixExpression(String op, Entity right) {
        switch (op) {
            case "-" -> { return evalMinusPrefixExpression(right); }
            case "!" -> { return evalBangExpression(right); }
            default -> { return newError("unknown operator: %s %s", op, right.Type()); }
        }
    }

    private static Entity evalMinusPrefixExpression(Entity right) {
        if (right.Type() != EntityType.INT_OBJ)
            return newError("unknown operator: -%s", right.Type());
        long val;
        if (right.Type() == EntityType.INT_OBJ) {
             val = ((Int) right).value();
             return new Int(-1*val);
        }
        else return NULL;
    }

    private static Entity evalBangExpression(Entity right) {
        if (right == TRUE)
            return FALSE;
        else if (right == FALSE)
            return TRUE;
        else if (right == NULL)
            return TRUE;
        else return FALSE;
    }

    private static Bool getBoolObject(boolean pValue) {
        if (pValue)
            return TRUE;
        else return FALSE;
    }

    private static Entity evalProgram(List<Statement> pStatements, Environment env) {
        Entity result = null;
        for (Statement stmt: pStatements) {
            result = eval(stmt, env);
            if (result != null) {
                if (result.getClass() == ReturnValue.class)
                    return ((ReturnValue) result).value();
                else if (result.getClass() == ErrorMsg.class)
                    return result;
            }
        }
        return result;
    }

    private static boolean isError(Entity obj) {
        if(obj != null)
            return obj.Type() == EntityType.ERROR_OBJ;
        return false;
    }

    private static ErrorMsg newError(String format, Object... entities) {
        return new ErrorMsg(String.format(format, entities));
    }
}
