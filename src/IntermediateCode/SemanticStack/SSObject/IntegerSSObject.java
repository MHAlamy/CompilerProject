package IntermediateCode.SemanticStack.SSObject;

/**
 * Created by mha on 1/27/18.
 */
public class IntegerSSObject extends SSObject {

    private int value;

    public IntegerSSObject(SSType type, int value) {
        super(type);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
