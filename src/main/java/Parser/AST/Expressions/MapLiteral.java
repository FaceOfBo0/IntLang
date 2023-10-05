package Parser.AST.Expressions;

import Lexer.Token.Token;
import Parser.AST.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record MapLiteral(Token tok, Map<Expression, Expression> elements) implements Expression {

    @Override
    public void expressionNode() { }

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
        List<String> pairs = new ArrayList<>();
        this.elements.forEach((key, value) -> pairs.add(key.toString() + ":" + value.toString()));
        return "{"+ String.join(", ", pairs) + "}";
    }
}
