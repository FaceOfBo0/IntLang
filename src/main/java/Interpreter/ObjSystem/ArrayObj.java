package Interpreter.ObjSystem;

import java.util.List;

public record ArrayObj(List<Entity> value) implements Entity {

    @Override
    public EntityType Type() {
        return EntityType.ARRAY_OBJ;
    }

    @Override
    public String Inspect() {
        var elems = this.value.stream().map(Entity::Inspect).toList();
        return "[" + String.join(", ", elems) + "]";
    }
}
