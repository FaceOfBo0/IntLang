package Interpreter.ObjSystem;

public record ErrorMsg(String message) implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.ERROR_OBJ;
    }

    @Override
    public String Inspect() {
        return "ERROR: " + this.message;
    }
}
