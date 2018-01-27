package SymbolTable.Row;

import SymbolTable.MethodSymbolTable;
import SymbolTable.SymbolTable;

import java.util.ArrayList;

/**
 * Created by mha on 1/27/18.
 */
public class MethodRow extends NonClassRow {
    private MethodSymbolTable methodSymbolTable;
    private ArrayList<Integer> parameterAdresses;

    public MethodRow(SymbolTable container, String name) {//, SymbolTable.MethodSymbolTable methodSymbolTable) {
        super(container, name);
//        this.methodSymbolTable = methodSymbolTable;
        parameterAdresses = new ArrayList<>();
    }

    public MethodSymbolTable getMethodSymbolTable() {
        return methodSymbolTable;
    }

    public void setMethodSymbolTable(MethodSymbolTable methodSymbolTable) {
        this.methodSymbolTable = methodSymbolTable;
    }

    public void addParamterAddress(int address) {
        parameterAdresses.add(address);
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.Row.MethodRow: " + getName() + ", and has type " + getRowType() + ". is in table : " + getContainer().getName() + "\n";
        if (methodSymbolTable != null) {
            res += "\t" + methodSymbolTable.toString().replaceAll("\\n", "\n\t");
        }
        res += "\n";

        return res;
    }
}
