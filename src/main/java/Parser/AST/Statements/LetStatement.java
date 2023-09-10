package Parser.AST.Statements;
import Parser.AST.Expression;
import Parser.AST.Expressions.Identifier;
import Parser.AST.Statement;
import Token.*;

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
