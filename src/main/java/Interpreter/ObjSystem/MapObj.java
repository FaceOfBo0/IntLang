package Interpreter.ObjSystem;

import java.util.Map;

public record MapObj(Map<Entity, Entity> value) implements Entity {
    @Override
    public EntityType Type() {
        return EntityType.MAP_OBJ;
    }

    @Override
    public String Inspect() {
        StringBuilder out = new StringBuilder("{");
        for (Map.Entry<Entity, Entity> entry : this.value.entrySet()){
            out.append(entry.getKey().Inspect()).append(":").append(entry.getValue().Inspect()).append(", ");
        }
        return out.substring(0, out.length() - 2) + "}";
    }
}
