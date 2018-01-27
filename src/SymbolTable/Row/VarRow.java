package SymbolTable.Row;
import SymbolTable.SymbolTable;

public class VarRow extends NonClassRow {
    public VarRow(SymbolTable container, String name) {
        super(container, name);
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.VarRow: " + getName() + ", and has type " + getType() + ". is in table : " +
                getContainer().getName() + " and address " + getAddress() + "\n";

        return res;
    }
}
