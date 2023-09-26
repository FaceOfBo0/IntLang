package Interpreter.ObjSystem;

public record ReturnValue(Entity value) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.RETURN_VALUE_OBJ;
    }

    @Override
    public String Inspect() {
        return this.value.Inspect();
    }
}
