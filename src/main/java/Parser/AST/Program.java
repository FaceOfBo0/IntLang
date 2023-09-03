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
        for (int i = 0; i < this.statements.size() - 1; i++)
            out.append(this.statements.get(i)).append("\n");
        out.append(this.statements.get((this.statements.size() - 1)));
        return out.toString();
    }
}
