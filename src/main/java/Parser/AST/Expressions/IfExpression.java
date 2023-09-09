package Parser.AST.Expressions;

import Parser.AST.Expression;
import Parser.AST.Statements.BlockStatement;
import Token.*;

public record IfExpression(Token tok, Expression condition, BlockStatement consequence, BlockStatement alternative) implements Expression {
    public static int nestLevel = 0;

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
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
        nestLevel++;
        StringBuilder out = new StringBuilder("If " + this.condition + " {\n" + this.consequence);
        if (this.alternative != null)
            out.append("\nelse {\n").append(this.alternative);
        nestLevel--;
        return out.toString();
    }
}
