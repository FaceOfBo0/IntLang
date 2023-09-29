package Interpreter.ObjSystem;

public record StringObj(String value) implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.STRING_OBJ;
    }

    @Override
    public String Inspect() {
        return "'" + this.value + "'";
    }
}
