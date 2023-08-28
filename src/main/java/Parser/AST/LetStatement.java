package Parser.AST;
import Token.*;

public class LetStatement implements Statement {
    private Expression value;
    private Identifier name;
    private final Token tok;

    public void setName(Identifier pIdent) {
        this.name = pIdent;
    }

    public void setValue(Expression pValue) {
        this.value = pValue;
    }

    public Expression getValue() {
        return this.value;
    }

    public Identifier getName() {
        return this.name;
    }

    public LetStatement(Token pToken) {
        this.tok = pToken;
    }
    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public void statementNode() {
    }

    @Override
    public String toString() {
        return "LetStatement:{"+this.tokenLiteral()+", "+this.name +", "+ this.value + "}";
    }
}
