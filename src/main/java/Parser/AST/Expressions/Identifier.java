package Parser.AST.Expressions;
import Lexer.Token.Token;
import Parser.AST.Expression;

public record Identifier(Token tok, String value) implements Expression {
    @Override
    public void expressionNode() {}

    @Override
    public String tokenLiteral() {
        return this.tok.literal();
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
