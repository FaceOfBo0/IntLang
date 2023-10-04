package Parser.AST.Expressions;

import Lexer.Token.Token;
import Parser.AST.Expression;
import Parser.AST.Statements.BlockStatement;

import java.util.List;

public record FunctionLiteral(Token tok, List<Identifier> parameters, BlockStatement body) implements Expression {

    public static Integer nestLevel = 0;

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
        nestLevel++;
        String strOut = "fn(" + String.join(", ", this.parameters) + ") {\n" + this.body;
        nestLevel--;
        return strOut;
    }
}
