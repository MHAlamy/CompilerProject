package IntermediateCode.SemanticStack.Object;

/**
 * Created by mha on 1/27/18.
 */
public class AddressSSObject extends SSObject {

    private int value;

    public AddressSSObject(int value) {
        super(SSType.ADDRESS);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
