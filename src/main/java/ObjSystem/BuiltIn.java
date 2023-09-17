package ObjSystem;

public record BuiltIn(BuiltInFunction fn) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.BUILTIN_OBJ;
    }

    @Override
    public String Inspect() {
        return "BuiltIn Function";
    }
}
