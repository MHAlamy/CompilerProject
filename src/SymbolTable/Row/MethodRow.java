package SymbolTable.Row;

import SymbolTable.MethodSymbolTable;
import SymbolTable.SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public class MethodRow extends NonClassRow {
    private MethodSymbolTable methodSymbolTable;

    public MethodRow(SymbolTable container, String name) {//, SymbolTable.MethodSymbolTable methodSymbolTable) {
        super(container, name);
//        this.methodSymbolTable = methodSymbolTable;
    }

    public MethodSymbolTable getMethodSymbolTable() {
        return methodSymbolTable;
    }

    public void setMethodSymbolTable(MethodSymbolTable methodSymbolTable) {
        this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.Row.MethodRow: " + getName() + ", and has type " + getType() + ". is in table : " + getContainer().getName() + "\n";
        if (methodSymbolTable != null) {
            res += "\t" + methodSymbolTable.toString().replaceAll("\\n", "\n\t");
        }
        res += "\n";

        return res;
    }
}
