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

    public void setName(String name) {
        this.name = name;
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
}

class ValueIndex extends Index {
    private int value;

    public ValueIndex(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
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

    @Override
    public String toString() {
        return row.getName().toString();
    }
}