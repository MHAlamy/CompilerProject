package IntermediateCode.SemanticStack.Object;

public class BooleanSSObject extends SSObject {

    private int value;

    public BooleanSSObject(int value) {
        super(SSType.BOOLEAN);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
