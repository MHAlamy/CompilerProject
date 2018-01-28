package IntermediateCode.SemanticStack.Object;


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

