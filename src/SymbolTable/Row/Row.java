package SymbolTable.Row;

import SymbolTable.SymbolTable;

/**
 * Created by mha on 1/27/18.
 */
public abstract class Row {
    private SymbolTable container;
    private String name;

    public Row(SymbolTable container, String name) {
        this.container = container;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SymbolTable getContainer() {
        return container;
    }

    @Override
    public boolean equals(Object obj) { // CONSIDERS ONLY NAME
        // TODO: 1/27/18 consider only name in method and var rows?
        //return (obj.getClass().equals(this.getClass())) &&
        return (Row.class.isAssignableFrom(obj.getClass())) &&
                (((Row) obj).getName().equals(this.getName())); // may consider type for supporting same names
    }

}
