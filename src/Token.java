import SymbolTable.Row.Row;

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
//    private boolean isInt;
//    private int value; // only if isInt
//    private SymbolTable.Row.Row rowPointer; // otherwise, if is ID

//    public Index(int value) {
//        isInt = true;
//        this.value = value;
//    }
//
//    public Index(SymbolTable.Row.Row rowPointer) {
//        isInt = false;
//        this.rowPointer = rowPointer;
//    }
    public Index() {

    }
//    public void setRowPointer(SymbolTable.Row.Row rowPointer) {
//        this.rowPointer = rowPointer;
//    }
//
//    public boolean isInt() {
//        return isInt;
//    }
//
//    public int getValue() {
//        return value;
//    }
//
//    public SymbolTable.Row.Row getRowPointer() {
//        return rowPointer;
//    }
}

class ValueIndex extends Index {
    private int value;

    public ValueIndex(int value) {
        this.value = value;

    }
}

class RowIndex extends Index {
    private Row row;

    public RowIndex(Row row) {
        this.row = row;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }
}