package IntermediateCode.SemanticStack.SSObject;

/**
 * Created by mha on 1/27/18.
 */
public class BooleanSSObject extends SSObject {

    private boolean value;

    public BooleanSSObject(SSType type, boolean value) {
        super(type);
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
