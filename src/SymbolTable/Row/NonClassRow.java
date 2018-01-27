package SymbolTable.Row;
import SymbolTable.SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public class
NonClassRow extends Row {
    private RowType rowType;
    private int address;

    public NonClassRow(SymbolTable container, String name) {
        super(container, name);
    }

    public RowType getRowType() {
        return rowType;
    }

    public void setRowType(RowType rowType) {
        this.rowType = rowType;
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
        res += "SymbolTable.Row.NonClassRow: " + getName() + ", and has rowType " + rowType + ". is in table : " + getContainer().getName() + "\n";

        return res;
    }
}
