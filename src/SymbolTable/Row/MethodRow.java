package SymbolTable.Row;

import SymbolTable.MethodSymbolTable;
import SymbolTable.SymbolTable;

import java.util.ArrayList;

/**
 * Created by mha on 1/27/18.
 */
public class MethodRow extends NonClassRow {
    private MethodSymbolTable methodSymbolTable;
    private ArrayList<Integer> parameterAddresses;
    private int returnValueAddress;
    private int returnJumpAddress;

    public MethodRow(SymbolTable container, String name) {//, SymbolTable.MethodSymbolTable methodSymbolTable) {
        super(container, name);
//        this.methodSymbolTable = methodSymbolTable;
        parameterAddresses = new ArrayList<>();
    }

    public MethodSymbolTable getMethodSymbolTable() {
        return methodSymbolTable;
    }

    public void setMethodSymbolTable(MethodSymbolTable methodSymbolTable) {
        this.methodSymbolTable = methodSymbolTable;
    }

    public void addParameterAddress(int address) {
        parameterAddresses.add(address);
    }

    public int getReturnValueAddress() {
        return returnValueAddress;
    }

    public void setReturnValueAddress(int returnValueAddress) {
        this.returnValueAddress = returnValueAddress;
    }

    public int getReturnJumpAddress() {
        return returnJumpAddress;
    }

    public void setReturnJumpAddress(int returnJumpAddress) {
        this.returnJumpAddress = returnJumpAddress;
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
