package Interpreter.ObjSystem;

import Parser.AST.Expressions.Identifier;
import Parser.AST.Statements.BlockStatement;

import java.util.List;

public record FunctionObj(List<Identifier> parameters, BlockStatement body, Environment env) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.FUNCTION_OBJ;
    }

    @Override
    public String Inspect() {
        return "fn(" +
                String.join(", ", this.parameters) +
                ") {\n" +
                this.body.toString();
    }
}
