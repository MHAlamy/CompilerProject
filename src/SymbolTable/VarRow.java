package SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public class VarRow extends NonClassRow {
    public VarRow(SymbolTable container, String name) {
        super(container, name);
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.VarRow: " + getName() + ", and has type " + getType() + ". is in table : " + getContainer().getName() + "\n";

        return res;
    }
}
