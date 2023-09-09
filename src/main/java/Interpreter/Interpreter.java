package Interpreter;

import Parser.AST.Expressions.*;
import Parser.AST.Statements.*;
import Parser.AST.*;
import ObjSystem.*;

import java.util.List;
import java.util.Objects;

public abstract class Interpreter {

    static Null NULL = new Null();
    static Bool TRUE = new Bool(true);
    static Bool FALSE = new Bool(false);

    public static Entity eval(Node pNode) {
        if (pNode.getClass().equals(Program.class))
            return evalStatements(((Program) pNode).getStatements());
        else if (pNode.getClass().equals(ExpressionStatement.class))
            return eval(((ExpressionStatement) pNode).value());
        else if (pNode.getClass() == BlockStatement.class) {
            return evalStatements(((BlockStatement) pNode).statements());
        }
        else if (pNode.getClass().equals(IntegerLiteral.class))
            return new Int(((IntegerLiteral) pNode).value());
        else if (pNode.getClass().equals(BooleanLiteral.class))
            return getBoolObject(((BooleanLiteral) pNode).value());
        else if (pNode.getClass() == PrefixExpression.class) {
            Entity right = eval(((PrefixExpression) pNode).right());
            return evalPrefixExpression(((PrefixExpression) pNode).op(), right);
        }
        else if (pNode.getClass() == InfixExpression.class) {
            Entity left = eval(((InfixExpression) pNode).left());
            Entity right = eval(((InfixExpression) pNode).right());
            return evalInfixExpression(((InfixExpression) pNode).op(), left, right);
        }
        else if (pNode.getClass() == IfExpression.class) {
            Entity condition = eval((((IfExpression) pNode).condition()));
            return evalIfExpression(condition, ((IfExpression) pNode).consequence(), ((IfExpression) pNode).alternative());
        }
        return NULL;
    }

    private static Entity evalIfExpression(Entity condition, BlockStatement consequence, BlockStatement alternative) {
        if (isTruthy(condition))
            return eval(consequence);
        else if (alternative != null)
            return eval(alternative);
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
        return NULL;
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
            default -> { return NULL; }
        }
    }

    private static Entity evalPrefixExpression(String op, Entity right) {
        switch (op) {
            case "-" -> { return evalMinusPrefixExpression(right); }
            case "!" -> { return evalBangExpression(right); }
            default -> { return NULL; }
    }
    }

    private static Entity evalMinusPrefixExpression(Entity right) {
        long val;
        if (right.Type() == EntityType.INT_OBJ){
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

    private static Entity evalStatements(List<Statement> pStatements) {
        Entity result = null;
        for (Statement stmt: pStatements) {
             result = eval(stmt);
        }
        return result;
    }
}
