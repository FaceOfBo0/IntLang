package Parser.AST;
import Token.*;
public class Identifier implements Expression {
    Token tok;

    @Override
    public void expressionNode() {
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }
}
