package Parser.AST;
import Token.*;
public class Identifier implements Expression {

    public Token tok = null;
    public String value;

    public Identifier(Token pTok, String pValue){
        this.tok = pTok;
        this.value = pValue;
    }

    @Override
    public void expressionNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    public String toString() {
        return "Identifier:{" + this.value + "}";
    }
}
