package Parser.AST.Statements;

import Parser.AST.Expressions.FunctionLiteral;
import Parser.AST.Expressions.IfExpression;
import Parser.AST.Statement;
import Token.*;

import java.util.ArrayList;
import java.util.List;

public record BlockStatement (Token tok, List<Statement> statements) implements Statement {

    @Override
    public String tokenLiteral() {
        return this.tok.literal();
    }
    @Override
    public void statementNode() { }

    @Override
    public String toString() {
        StringBuilder outString = new StringBuilder();
        String tabsLevel = "\t";
        tabsLevel = tabsLevel.repeat(IfExpression.nestLevel+ FunctionLiteral.nestLevel);
        for (Statement stmt: this.statements)
            outString.append(tabsLevel).append(stmt).append("\n");
        outString.append(tabsLevel, 0, tabsLevel.length()-1).append("}");
        return outString.toString();
    }
}
