package Parser.AST.Statements;
import Lexer.Token.Token;
import Parser.AST.Expression;
import Parser.AST.Expressions.Identifier;
import Parser.AST.Statement;

public record LetStatement(Token tok, Identifier name, Expression value) implements Statement {

    @Override
    public String tokenLiteral() {
        return this.tok.literal();
    }

    @Override
    public void statementNode() {}

    @Override
    public String toString() {
        return this.tokenLiteral() + " " + name + " = " + value + ";";
    }
}
