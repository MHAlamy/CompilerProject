package IntermediateCode.SemanticStack.Object;


public class IntegerSSObject extends SSObject {

    private int value;

    public IntegerSSObject(int value) {
        super(SSType.INTEGER);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
