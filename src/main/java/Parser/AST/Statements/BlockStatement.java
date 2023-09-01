package Parser.AST.Statements;

import Parser.AST.Statement;
import Token.*;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements Statement {
    Token tok;
    List<Statement> statements = new ArrayList<>(0);

    public BlockStatement(Token tok) {
        this.tok = tok;
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    public void addStatement(Statement stmt) {
        this.statements.add(stmt);
    }

    @Override
    public String tokenLiteral() {
        return this.tok.literal;
    }

    @Override
    public void statementNode() { }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("{\n");
        this.statements.forEach(elem -> out.append(elem).append("\n"));
        out.append("}");
        return out.toString();
    }
}
