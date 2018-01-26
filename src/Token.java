public class Token {
    private String name;
    private Index index;

    public Token(String key, Index index) {
        this.name = key;
        this.index = index;
    }

    public Token(String key) {
        this.name = key;
    }

    public String getName() {
        return name;
    }

    public Index getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "<" + name + ", " + index + ">";
    }
}

class Index {
    private boolean isInt;
    private int value; // only if isInt
    private SymbolRow rowPointer; // otherwise, if is ID

    public Index(int value) {
        isInt = true;
        this.value = value;
    }

    public Index(SymbolRow rowPointer) {
        isInt = false;
        this.rowPointer = rowPointer;
    }

    public void setRowPointer(SymbolRow rowPointer) {
        this.rowPointer = rowPointer;
    }

    public boolean isInt() {
        return isInt;
    }

    public int getValue() {
        return value;
    }

    public SymbolRow getRowPointer() {
        return rowPointer;
    }
}