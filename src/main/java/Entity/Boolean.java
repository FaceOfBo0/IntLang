package Entity;

public class Boolean implements Entity {
    boolean value;
    @Override
    public EntityType Type() {
        return EntityType.BOOLEAN_OBJ;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String Inspect() {
        return String.valueOf(this.value);
    }
}
