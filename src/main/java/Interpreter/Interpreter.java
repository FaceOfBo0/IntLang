package Interpreter;

import Parser.AST.Expressions.*;
import Parser.AST.Statements.*;
import Parser.AST.*;
import Entity.*;

import java.util.List;

public abstract class Interpreter {

    static Null NULL = new Null();
    static Bool TRUE = new Bool(true);
    static Bool FALSE = new Bool(false);

    public static Entity eval(Node pNode) {
        if (pNode.getClass().equals(Program.class))
            return evalStatements(((Program) pNode).getStatements());
        else if (pNode.getClass().equals(ExpressionStatement.class))
            return eval(((ExpressionStatement) pNode).value());
        else if (pNode.getClass().equals(IntegerLiteral.class))
            return new Int(((IntegerLiteral) pNode).value());
        else if (pNode.getClass().equals(BooleanLiteral.class))
            return getBoolObject(((BooleanLiteral) pNode).value());
        else if (pNode.getClass() == PrefixExpression.class) {
            Entity right = eval(((PrefixExpression) pNode).getRight());
            return evalPrefixExpression(((PrefixExpression) pNode).getOp(), right);
        }
        return NULL;
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
