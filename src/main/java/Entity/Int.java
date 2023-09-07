package Entity;

public record Int(long value) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.INT_OBJ;
    }

    @Override
    public String Inspect() {
        return String.valueOf(this.value);
    }
}
