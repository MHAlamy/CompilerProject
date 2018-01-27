package SymbolTable;

import java.util.ArrayList;

/**
 * Created by mha on 1/27/18.
 */
public class MasterSymbolTable extends SymbolTable {

    private ArrayList<ClassRow> classRows;

    public MasterSymbolTable(String name) {
        super(name);
        classRows = new ArrayList<ClassRow>();
    }

    public ArrayList<ClassRow> getClassRows() {
        return classRows;
    }

    @Override
    public ClassRow getRow(Row idRow) { //throws Exception {
        ClassRow res;
//        int rowNum = nonClassRows.indexOf(new SymbolTable.NonClassRow(this, name));
        int rowNum = classRows.indexOf(idRow);

        if (rowNum >= 0)
            res = classRows.get(rowNum);
        else
            res = null; // was not found

        return res;
    }

    @Override
    public ClassRow insertRow(Row idRow) {
//        SymbolTable.ClassSymbolTable classSymbolTable = new SymbolTable.ClassSymbolTable(name, this);
//        SymbolTable.ClassRow temp = new SymbolTable.ClassRow(this, name, classSymbolTable);
        classRows.add((ClassRow)idRow);
        return (ClassRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("SymbolTable.MasterSymbolTable : " + getName() + "\n-----------------------------------------------\n");
        for (Row row :
                classRows) {
            res += row;
        }
        return res;
    }
}
