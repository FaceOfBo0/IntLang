package Parser.AST.Expressions;

import Parser.AST.Expression;
import Token.*;

public class IntegerLiteral implements Expression {
    Token tok;
    Long value;

    public IntegerLiteral(Token pToken) {
        this.tok = pToken;
        this.value = null;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public String toString() {
        return this.tok.literal;
    }
}
