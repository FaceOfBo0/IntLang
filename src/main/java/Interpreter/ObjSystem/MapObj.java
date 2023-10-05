package Interpreter.ObjSystem;

import java.util.Map;

public record MapObj(Map<Entity, Entity> value) implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.MAP_OBJ;
    }

    @Override
    public String Inspect() {
        return this.value.toString().replace("=", ":");
    }
}
