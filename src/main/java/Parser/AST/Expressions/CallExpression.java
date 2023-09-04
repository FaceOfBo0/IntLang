package Parser.AST.Expressions;

import Parser.AST.Expression;
import Token.*;
import java.util.List;

public class CallExpression implements Expression {
    Token tok;
    Expression function;
    List<Expression> params;

    public CallExpression(Token tok) {
        this.tok = tok;
    }

    public void setFnIdent(Expression pFunction) {
        this.function = pFunction;
    }

    public void setParams(List<Expression> params) {
        this.params = params;
    }

    @Override
    public void expressionNode() { }

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
    public String tokenLiteral() {
        return this.function + "(" + String.join(", ", this.params) + ")";
    }
}
