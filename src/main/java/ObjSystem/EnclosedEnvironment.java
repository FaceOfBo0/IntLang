package ObjSystem;

public class EnclosedEnvironment extends Environment {

    public EnclosedEnvironment(Environment pOuter) {
        super();
        this.outer = pOuter;
    }
}
