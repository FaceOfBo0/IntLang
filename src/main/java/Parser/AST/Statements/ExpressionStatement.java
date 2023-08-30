package Parser.AST.Statements;

import Parser.AST.*;
import Token.*;

public class ExpressionStatement implements Statement {
    Token tok;
    Expression value;

    public ExpressionStatement(Token pToken) {
        this.tok = pToken;
        this.value = null;
    }

    public void setValue(Expression value) {
        this.value = value;
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public void statementNode() {}

    @Override
    public String toString() {
        if (this.value != null)
            return this.value.toString();
        else return "";
    }
}
