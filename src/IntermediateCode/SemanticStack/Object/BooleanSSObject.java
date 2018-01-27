package IntermediateCode.SemanticStack.Object;

/**
 * Created by mha on 1/27/18.
 */
public class BooleanSSObject extends SSObject {

    private int value;

    public BooleanSSObject(SSType type, int value) {
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
