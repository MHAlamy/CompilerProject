package SymbolTable;

import SymbolTable.Row.MethodRow;
import SymbolTable.Row.NonClassRow;
import SymbolTable.Row.Row;
import SymbolTable.Row.VarRow;

import java.util.ArrayList;

/**
 * Created by mha on 1/27/18.
 */
public class ClassSymbolTable extends SymbolTable {

    private MasterSymbolTable masterSymbolTable;
    private ClassSymbolTable parentClass;
    private ArrayList<NonClassRow> nonClassRows;

    public ClassSymbolTable(String name, MasterSymbolTable masterSymbolTable) {
        super(name);
        this.masterSymbolTable = masterSymbolTable;
        parentClass = null;
        nonClassRows = new ArrayList<NonClassRow>();
    }

    public ClassSymbolTable getParentClass() {
        return parentClass;
    }

    public void setParentClass(ClassSymbolTable parentClass) {
        this.parentClass = parentClass;
    }

    public ArrayList<NonClassRow> getNonClassRows() {
        return nonClassRows;
    }

    @Override
    public NonClassRow getRow(Row idRow) {
        NonClassRow res;

//        int rowNum = nonClassRows.indexOf(new SymbolTable.Row.NonClassRow(this, name));
        int rowNum = nonClassRows.indexOf(idRow); // ???

        if (rowNum >= 0)
            res = nonClassRows.get(rowNum);
        else
            res = null; // was not found

        return res;
    }

    @Override
    public NonClassRow insertRow(Row idRow) {// TODO: 1/27/18 NEW METHOD OR VARIABLE???

        if (idRow.getClass().equals(VarRow.class))
            nonClassRows.add((VarRow)idRow);
        else if (idRow.getClass().equals(MethodRow.class))
            nonClassRows.add((MethodRow)idRow);
        else
            System.out.println("ERROR! SymbolTable.Row.Row inserted inside SymbolTable.ClassSymbolTable is INVALID");

        return (NonClassRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("SymbolTable.ClassSymbolTable : " + getName() + ". Parent is = " +
                ((parentClass == null) ? ("NULL") : (parentClass.getName())) +
                "\n-----------------------------------------------\n");
        for (Row row :
                nonClassRows) {
            res += row;
        }
        return res;
    }
}
