package Parser.AST;
import Token.*;

public class LetStatement implements Statement {
    Expression value;
    Identifier name;
    Token tok;

    public LetStatement(Token pToken, Identifier pName, Expression pValue) {
        this.value = pValue;
        this.name = pName;
        this.tok = pToken;
    }
    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public void statementNode() {
    }
}
