package IntermediateCode.SemanticStack.Object;

/**
 * Created by mha on 1/27/18.
 */
public class StringSSObject extends SSObject {

    private String value;

    public StringSSObject(String value) {
        super(SSType.STRING);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
