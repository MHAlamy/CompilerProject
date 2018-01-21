import java.util.ArrayList;

public class SymbolTable {
    private SymbolTable container;
    private String name;
    private boolean isClass;

    ArrayList<SymbolRow> symbolRows;

    public SymbolTable(SymbolTable container, boolean isClass) {
        this.container = container;
        this.isClass = isClass;
        symbolRows = new ArrayList<SymbolRow>();
    }
    public SymbolTable(boolean isClass) {
        container = null;
        this.isClass = isClass;
        symbolRows = new ArrayList<SymbolRow>();
    }

    public SymbolTable getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public boolean isClass() {
        return isClass;
    }

    public int getIdIndex(String inputId) { // returns -1 if id is not already defined
        return symbolRows.indexOf(new SymbolRow(inputId));
    }

    public int insertId(String inputId) { // returns added id's index
        symbolRows.add(new SymbolRow(inputId));
        return symbolRows.size() - 1; // insertId is added at the end of list
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(SymbolTable.class)) {
            SymbolTable tmp = (SymbolTable)obj;
            return this.name.equals(tmp.getName()) && this.isClass == tmp.isClass();
        }
        return false;
    }
}

class SymbolRow {
    private String idName;
    private ArrayList<String> attributes;

    public SymbolRow(String idName) {
        this.idName = idName;
        attributes = new ArrayList<String>();
    }

    public String getIdName() {
        return idName;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.getClass().equals(SymbolRow.class)) &&
                (((SymbolRow)obj).getIdName().equals(this.getIdName()));


    }
}