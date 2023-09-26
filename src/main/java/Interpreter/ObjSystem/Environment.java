package Interpreter.ObjSystem;

import java.util.HashMap;
import java.util.Map;
import static Interpreter.Interpreter.NULL;

public class Environment {
    Map<String, Entity> store;
    Environment outer = null;

    public Environment(){
        this.store = new HashMap<>(0);
    }

    public Entity get(String key) {
        Entity result = this.store.getOrDefault(key, NULL);
        if (result == NULL && this.outer != null) {
            return this.outer.get(key);
        }
        return result;
    }

    public void set(String key, Entity value) {
        this.store.put(key, value);
    }
}
