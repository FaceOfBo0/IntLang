package Parser.AST.Expressions;

import Parser.AST.Expression;
import Parser.AST.Statements.BlockStatement;
import Token.*;

import java.util.ArrayList;
import java.util.List;

public class FunctionLiteral implements Expression {
    Token tok;
    List<Identifier> parameters = new ArrayList<>(0);
    BlockStatement body;
    public static Integer nestLevel = 0;

    public FunctionLiteral(Token tok) {
        this.tok = tok;
    }

    public void setParameters(List<Identifier> parameters) {
        this.parameters = parameters;
    }

    public void setBody(BlockStatement body) {
        this.body = body;
    }

    @Override
    public void expressionNode() {

    }

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
        String strOut = "fn(" + String.join(", ", this.parameters) + ") {\n" + this.body;
        nestLevel--;
        return strOut;
    }
}
