package Parser.AST.Statements;

import Lexer.Token.Token;
import Parser.AST.Expression;
import Parser.AST.Statement;

public record ReturnStatement(Token tok, Expression value) implements Statement {

    @Override
    public String tokenLiteral() {
        return tok.literal();
    }

    @Override
    public void statementNode() {}

    @Override
    public String toString() {
        return this.tokenLiteral() + " " + this.value + ";";
    }
}
