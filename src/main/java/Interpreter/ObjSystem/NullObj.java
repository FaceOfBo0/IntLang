package Interpreter.ObjSystem;

public class NullObj implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.NULL_OBJ;
    }

    @Override
    public String Inspect() {
        return "NULL";
    }
}
