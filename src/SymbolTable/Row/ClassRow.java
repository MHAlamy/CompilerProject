package SymbolTable.Row;

import SymbolTable.ClassSymbolTable;
import SymbolTable.SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public class ClassRow extends Row {
    private ClassSymbolTable classSymbolTable;

    public ClassRow(SymbolTable container, String name, ClassSymbolTable classSymbolTable) {
        super(container, name);
        this.classSymbolTable = classSymbolTable;
    }

    public ClassSymbolTable getClassSymbolTable() {
        return classSymbolTable;
    }

    @Override
    public String toString() {
        String res = "";
        res += "SymbolTable.Row.ClassRow: " + getName() + ". is in table : " + getContainer().getName() + "\n";
        if (classSymbolTable != null) {
            res += "\t" + classSymbolTable.toString().replaceAll("\\n", "\n\t");
        }
        res += "\n";

        return res;
    }
}
