import java.util.ArrayList;

public class SymbolTable {
    private SymbolTable container;
    private String name;
    private boolean isClass;

    ArrayList<SymbolRow> symbolRows;

    public SymbolTable(SymbolTable container) {
        this.container = container;
        symbolRows = new ArrayList<SymbolRow>();
    }
//    public SymbolTable(boolean isClass) {
//        container = null;
//        this.isClass = isClass;
//        symbolRows = new ArrayList<SymbolRow>();
//    }

    public SymbolTable getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    public boolean isClass() {
        return isClass;
    }

    public void setClass(boolean aClass) {
        isClass = aClass;
    }

    public Index getIdIndex(String inputId) { // returns -1 if id is not already defined
        Index res;
        int rowNum = symbolRows.indexOf(new SymbolRow(this, inputId));

        if (rowNum >= 0) {
            res = new Index(symbolRows.get(rowNum));
        } else {
            res = null; // was not found
        }

        return res;
    }

    public Index insertId(String inputId) { // returns added id's index
        symbolRows.add(new SymbolRow(this, inputId));
        return new Index(symbolRows.get(symbolRows.size() - 1)); // insertId is added at the end of list
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(SymbolTable.class)) {
            SymbolTable tmp = (SymbolTable)obj;
            return this.name.equals(tmp.getName()) && this.isClass == tmp.isClass();
        }
        return false;
    }

    @Override
    public String toString() {
        return ("Symbol table : " + name);
    }
}

class SymbolRow {
    private SymbolTable symbolTable;
    private String idName;

    private String type; // class, func, var

    private SymbolTable target; // only if is class or func;

    private ArrayList<String> attributes; // ???


    public SymbolRow(SymbolTable symbolTable, String idName) {
        this.symbolTable = symbolTable;
        this.idName = idName;
        attributes = new ArrayList<String>();
    }

    public String getIdName() {
        return idName;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SymbolTable getTarget() {
        return target;
    }

    public void setTarget(SymbolTable target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object obj) { // CONSIDERS ONLY NAME
        return (obj.getClass().equals(this.getClass())) &&
                (((SymbolRow)obj).getIdName().equals(this.getIdName())); // may consider type for supporting same names
    }

    @Override
    public String toString() {
        return ("ID " + idName + " in : " + symbolTable);
    }
}