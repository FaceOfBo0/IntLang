package Parser.AST.Expressions;

import Parser.AST.*;
import Token.*;

public class InfixExpression implements Expression{
    Token tok;
    Expression left;
    String op;
    Expression right;

    public InfixExpression(Token tok) {
        this.tok = tok;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public String toString() {
        return  "(" + left + " " + op + " " + right + ")";
    }
}
