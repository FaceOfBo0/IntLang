package Parser.AST.Statements;
import Parser.AST.Expression;
import Parser.AST.Expressions.Identifier;
import Parser.AST.Statement;
import Token.*;

public class LetStatement implements Statement {
    private final Token tok;
    private Identifier name;
    private Expression value;

    public LetStatement(Token pToken) {
        this.tok = pToken;
        this.value = null;
    }

    public void setName(Identifier pIdent) {
        this.name = pIdent;
    }

    public void setValue(Expression pValue) {
        this.value = pValue;
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public void statementNode() {}

    @Override
    public String toString() {
        return this.tokenLiteral() + " " + name + " = " + value + ";";
    }
}
