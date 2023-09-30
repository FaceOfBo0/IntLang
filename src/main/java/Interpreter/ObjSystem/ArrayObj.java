package Interpreter.ObjSystem;

import java.util.ArrayList;
import java.util.List;

public class ArrayObj implements Entity {

    List<Entity> value = new ArrayList<>(0);

    public ArrayObj(){
    }
    public ArrayObj(List<Entity> pList) {
        this.value = pList;
    }

    public List<Entity> value(){
        return this.value;
    }

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
