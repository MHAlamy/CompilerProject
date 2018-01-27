package IntermediateCode.SemanticStack.SSObject;

/**
 * Created by mha on 1/27/18.
 */
public class StringSSObject extends SSObject {

    private String value;

    public StringSSObject(SSType type, String value) {
        super(type);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
