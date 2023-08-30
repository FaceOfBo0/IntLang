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
    public String toString() {
        return "(" + op + right + ")";
    }
}
