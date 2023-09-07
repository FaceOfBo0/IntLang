package Parser.AST.Expressions;

import Parser.AST.Expression;
import Token.*;

public class PrefixExpression implements Expression {
    Token tok;
    String op;
    Expression right;

    public PrefixExpression(Token tok) {
        this.tok = tok;
    }

    public String getOp() {
        return op;
    }

    public Expression getRight() {
        return right;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    @Override
    public void expressionNode() {}

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public int length() {
        return this.toString().length();
    }

    @Override
    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.toString().subSequence(start, end);
    }

    @Override
    public String toString() {
        return "(" + op + right + ")";
    }
}