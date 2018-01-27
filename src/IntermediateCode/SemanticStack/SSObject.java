package IntermediateCode.SemanticStack;

public abstract class SSObject {

    private SSType type;

    public SSObject(SSType type) {
        this.type = type;
    }

    public SSType getType() {
        return type;
    }

    public void setType(SSType type) {
        this.type = type;
    }

}

class IntegerSSObject extends SSObject {

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

class BooleanSSObject extends SSObject {

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

class StringSSObject extends SSObject {

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

enum SSType {
    ADDRESS, INTEGER, BOOLEAN, STRING
}