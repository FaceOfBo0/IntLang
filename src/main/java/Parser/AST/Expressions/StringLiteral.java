package Parser.AST.Expressions;

import Parser.AST.Expression;
import Lexer.Token.Token;

public record StringLiteral(Token tok, String value) implements Expression {

    @Override
    public void expressionNode() {

    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal();
    }

    @Override
    public int length() {
        return this.toString().length();
    }

    @Override
    public char charAt(int index) {
        return this.toString().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return this.toString().subSequence(start, end);
    }

    @Override
    public String toString() {
        return "'" + this.tok.literal() + "'";
    }
}
