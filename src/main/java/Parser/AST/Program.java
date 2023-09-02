package Parser.AST;

import java.util.ArrayList;
import java.util.List;

public class Program implements Node {

    public List<Statement> statements = new ArrayList<>(0);

    @Override
    public String tokenLiteral() {
        if (!this.statements.isEmpty())
            return this.statements.get(0).tokenLiteral();
        else
            return "";
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        this.statements.forEach(elem -> out.append("\n").append(elem));
        return out.toString();
    }
}
