package Parser.AST.Expressions;

import Parser.AST.Expression;
import Parser.AST.Statement;
import Parser.AST.Statements.BlockStatement;
import Token.*;

public class IfExpression implements Expression {
    Token tok;
    public Expression condition;
    public BlockStatement consequence;
    public BlockStatement alternative;
    public static int nestLevel = 0;

    public IfExpression(Token tok) {
        this.tok = tok;
    }

    @Override
    public void expressionNode() { }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
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
