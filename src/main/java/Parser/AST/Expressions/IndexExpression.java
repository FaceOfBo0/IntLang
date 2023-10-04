package Parser.AST.Expressions;

import Lexer.Token.Token;
import Parser.AST.Expression;

public record IndexExpression(Token tok, Expression left, Expression index) implements Expression {

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
        return this.left + "[" + this.index + "]";
    }
}
