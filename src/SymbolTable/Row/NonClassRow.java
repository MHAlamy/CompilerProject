package SymbolTable.Row;
import SymbolTable.SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public class
NonClassRow extends Row {
    private Type type;
    private int address;

    public NonClassRow(SymbolTable container, String name) {
        super(container, name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.Row.NonClassRow: " + getName() + ", and has type " + type + ". is in table : " + getContainer().getName() + "\n";

        return res;
    }
}
