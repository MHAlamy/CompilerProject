package SymbolTable;

import java.util.ArrayList;

public abstract class SymbolTable {

    private String name;

    public SymbolTable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Row getRow(Row idRow);// throws Exception;

    public abstract Row insertRow(Row idRow);

    @Override
    public boolean equals(Object obj) {
//        if (obj.getClass().equals(SymbolTable.SymbolTable.class)) {
        if (SymbolTable.class.isAssignableFrom(obj.getClass())) {
            SymbolTable tmp = (SymbolTable) obj;
            return this.name.equals(tmp.getName());
        }
        return false;
    }
}

