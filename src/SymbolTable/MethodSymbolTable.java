package SymbolTable;

import java.util.ArrayList;

/**
 * Created by mha on 1/27/18.
 */
public class MethodSymbolTable extends SymbolTable {

    private ClassSymbolTable containerClass;
    private ArrayList<VarRow> varRows;

    public MethodSymbolTable(String name, ClassSymbolTable containerClass) {
        super(name);
        this.containerClass = containerClass;
        varRows = new ArrayList<VarRow>();
    }

    public ClassSymbolTable getContainerClass() {
        return containerClass;
    }

    public ArrayList<VarRow> getVarRows() {
        return varRows;
    }

    @Override
    public VarRow getRow(Row idRow) {// throws Exception {
        VarRow res;
//        int rowNum = nonClassRows.indexOf(new SymbolTable.NonClassRow(this, name));
        int rowNum = varRows.indexOf(idRow);

        if (rowNum >= 0)
            res = varRows.get(rowNum);
        else
            res = null; // was not found

        return res;
    }

    @Override
    public VarRow insertRow(Row idRow) {
//        SymbolTable.VarRow temp = new SymbolTable.VarRow(this, name);
        varRows.add((VarRow)idRow);
        return (VarRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("SymbolTable.MethodSymbolTable : " + getName() + ". Container is = " +
                ((containerClass == null) ? ("NULL") : (containerClass.getName())) + "\n-----------------------------------------------\n");
        for (Row row :
                varRows) {
            res += row;
        }
        return res;
    }
}
