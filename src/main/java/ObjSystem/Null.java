package ObjSystem;

public class Null implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.NULL_OBJ;
    }

    @Override
    public String Inspect() {
        return "NULL";
    }
}
