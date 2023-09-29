package Interpreter;

import Interpreter.ObjSystem.*;
import Parser.AST.Expressions.*;
import Parser.AST.Statements.*;
import Parser.AST.*;

import java.util.*;

public abstract class Interpreter {

    public static Null NULL = new Null();
    static Bool TRUE = new Bool(true);
    static Bool FALSE = new Bool(false);
    static Map<String, Entity> builtins = new HashMap<>(0);

    public static Entity eval(Node pNode, Environment env) {
        initBuiltIns();

        // Whole program
        if (pNode.getClass() == Program.class)
            return evalProgram(((Program) pNode).getStatements(), env);

        // Expression statements
        else if (pNode.getClass() == ExpressionStatement.class)
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
        else if (pNode.getClass() == IntegerLiteral.class)
            return new IntegerObj(((IntegerLiteral) pNode).value());

        // Boolean Literals
        else if (pNode.getClass() == BooleanLiteral.class)
            return getBoolObject(((BooleanLiteral) pNode).value());

        // String Literals
        else if (pNode.getClass() == StringLiteral.class)
            return new StringObj(((StringLiteral) pNode).value());

        // Array Literals
        else if (pNode.getClass() == ArrayLiteral.class) {
            List<Entity> elements = evalExpressions(((ArrayLiteral) pNode).elements(), env);
            if (elements.size() == 1 && isError(elements.get(0)))
                return elements.get(0);
            return new ArrayObj(elements);
        }

        // Index Expressions
        else if (pNode.getClass() == IndexExpression.class) {
            Entity left = eval(((IndexExpression) pNode).left(), env);
            if (isError(left))
                return left;
            if (left.Type() != EntityType.ARRAY_OBJ)
                return newError("[] can't be used on that type - expected: ARRAY, got: %s", left.Type());
            Entity index = eval(((IndexExpression) pNode).index(), env);
            if (isError(index))
                return index;
            if(index.Type() != EntityType.INT_OBJ)
                return newError("Type mismatch on index value - expected: INT, got %s", index.Type());
            return evalIndexExpression(left, index);
        }

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
            List<Entity> args = evalExpressions(((CallExpression) pNode).params(), env);
            if (args.size() == 1 && isError(args.get(0)))
                return args.get(0);
            return evalFunction(func, args);
        }
        // default
        return NULL;
    }

    private static void initBuiltIns() {
        // len() for Strings and Arrays
        BuiltInFunction lenBuiltInFn = (Entity... args) -> {
            if (args.length == 1) {
                if (args[0].Type() == EntityType.STRING_OBJ)
                    return new IntegerObj(args[0].Inspect().length());
                else if (args[0].Type() == EntityType.ARRAY_OBJ)
                    return new IntegerObj(((ArrayObj)args[0]).value().size());
                else return newError("wrong type of argument for 'len'; expected: STRING, got: %s", args[0].Type());
            }
            return newError("wrong number of arguments; got: %d, want: 1",args.length);
        };
        builtins.put("len", new BuiltIn(lenBuiltInFn));
        // first() for Arrays

    }

    private static Entity evalIndexExpression(Entity left, Entity index) {
        assert left.Type() == EntityType.ARRAY_OBJ;
        assert index.Type() == EntityType.INT_OBJ;
        var indexInt = ((IntegerObj) index).value();
        var maxIndex = ((ArrayObj) left).value().size() - 1;
        if (indexInt > maxIndex)
            return newError("IndexOutOfBounds - max index: %s, got: %s",maxIndex, indexInt);
        return ((ArrayObj) left).value().get(indexInt);
    }

    private static Entity evalFunction(Entity func, List<Entity> args) {
        if (func.getClass() == BuiltIn.class)
            return ((BuiltIn) func).fn().parse(args.toArray(new Entity[0]));
        else if (func.getClass() == Function.class) {
            Environment extendedEnv = extendedFunctionEnv(func, args);
            Entity evalBody = eval(((Function) func).body(), extendedEnv);
            assert evalBody != null;
            return unwrapReturnVal(evalBody);
        }
        else return newError("not a function: %s", func.Type());
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

    private static List<Entity> evalExpressions(List<Expression> params, Environment env) {
        List<Entity> expressions = new ArrayList<>(0);
        for (Expression arg: params) {
            Entity result = eval(arg, env);
            if (isError(result)){
                return new ArrayList<>(List.of(result));
            }
            expressions.add(result);
        }
        return expressions;
    }

    private static Entity evalIdentifier(Node pNode, Environment env) {
        Entity result = env.get(((Identifier) pNode).value());
        if (result != NULL)
            return result;
        result = builtins.getOrDefault(((Identifier) pNode).value(), NULL);
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
            return evalIntegerInfixExpression(op, left, right);
        if (left.Type() == EntityType.STRING_OBJ && right.Type() == EntityType.STRING_OBJ)
            return evalStringInfixExpression(op, left, right);
        else if (Objects.equals(op, "!="))
            return getBoolObject(left != right);
        else if (Objects.equals(op, "=="))
            return getBoolObject(left == right);
        else if (left.Type() != right.Type())
            return newError("type mismatch: %s %s %s", left.Type(), op, right.Type());
        else
            return newError("unknown operator: %s %s %s", left.Type(), op, right.Type());
    }

    private static Entity evalStringInfixExpression(String op, Entity left, Entity right) {
        String leftVal = ((StringObj) left).value();
        String rightVal = ((StringObj) right).value();
        switch (op) {
            case "+" -> { return new StringObj(leftVal + rightVal); }
            default -> { return newError("unknown operator: %s %s %s", left.Type(), op, right.Type()); }
        }
    }

    private static Entity evalIntegerInfixExpression(String op, Entity left, Entity right) {
        int leftVal = ((IntegerObj) left).value();
        int rightVal = ((IntegerObj) right).value();
        switch (op) {
            case "+" -> {return new IntegerObj(leftVal + rightVal);}
            case "-" -> {return new IntegerObj(leftVal - rightVal);}
            case "*" -> {return new IntegerObj(leftVal * rightVal);}
            case "/" -> {return new IntegerObj(leftVal / rightVal);}
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
        int val;
        if (right.Type() == EntityType.INT_OBJ) {
             val = ((IntegerObj) right).value();
             return new IntegerObj(-1 * val);
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
