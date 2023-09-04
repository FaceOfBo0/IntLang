package Parser.AST.Expressions;
import Parser.AST.Expression;
import Token.*;

public class Identifier implements Expression {

    public Token tok;
    public String value;

    public Identifier(Token pTok){
        this.tok = pTok;
        this.value = this.tok.literal;
    }

    @Override
    public void expressionNode() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String pValue) {
        this.value = pValue;
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public char charAt(int index) {
        return this.value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }
}
