package Parser.AST.Statements;

import Parser.AST.Expression;
import Parser.AST.Statement;
import Token.*;

public class ReturnStatement implements Statement {
    Token tok;
    Expression value;

    public ReturnStatement(Token pToken){
        tok = pToken;
    }

    public void setValue(Expression pValue) {
        this.value = pValue;
    }

    public Expression getValue() {
        return this.value;
    }

    @Override
    public String tokenLiteral() {
        return tok.literal;
    }

    @Override
    public void statementNode() {}

    @Override
    public String toString() {
        return "ReturnStatement:{" + tok.type + ", " + value + '}';
    }
}