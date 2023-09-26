package Interpreter.ObjSystem;

public record Bool(boolean value) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.BOOLEAN_OBJ;
    }

    @Override
    public String Inspect() {
        return String.valueOf(this.value);
    }
}
