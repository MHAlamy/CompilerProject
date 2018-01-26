import java.util.ArrayList;

public abstract class SymbolTable {
//    private SymbolTable container;
    private String name;
//    private boolean isClass;

//    ArrayList<Row> rows;

//    public SymbolTable(SymbolTable container) {
//        this.container = container;
//        rows = new ArrayList<Row>();
//    }

//    public SymbolTable(boolean isClass) {
//        container = null;
//        this.isClass = isClass;
//        rows = new ArrayList<Row>();
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Row getIdIndex(String inputId); // returns -1 if id is not already defined
//        Index res;
//        int rowNum = rows.indexOf(new Row(this, inputId));
//
//        if (rowNum >= 0) {
//            res = new Index(rows.get(rowNum));
//        } else {
//            res = null; // was not found
//        }
//
//        return res;
//    }

    public abstract Row insertId(String inputId);// { // returns added id's index
//        rows.add(new Row(this, inputId));
//        return new Index(rows.get(rows.size() - 1)); // insertId is added at the end of list
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(SymbolTable.class)) {
            SymbolTable tmp = (SymbolTable)obj;
            return this.name.equals(tmp.getName());
        }
        return false;
    }

//    @Override
//    public String toString() {
//        String res = "";
//        res += ("Symbol table : " + name + ". Container is = " +
//                ((container == null) ? ("NULL") : (container.getName())) + "\n-----------------------------------------------\n");
//        for (Row row :
//                rows) {
//            res += row;
//        }
//        return res;
//    }
}



class ClassSymbolTable extends SymbolTable {
    private ClassSymbolTable parentClass;
    private ArrayList<NonClassRow> nonClassRows;

    public ClassSymbolTable() {
        nonClassRows = new ArrayList<NonClassRow>();
    }

    public void setParentClass(ClassSymbolTable parentClass) {
        this.parentClass = parentClass;
    }
    public ClassSymbolTable getParentClass() {
        return parentClass;
    }

    public ArrayList<NonClassRow> getNonClassRows() {
        return nonClassRows;
    }

    @Override
    public Row getIdIndex(String inputId) {
        Row res;

        int rowNum = nonClassRows.indexOf(new NonClassRow(this, inputId));

        if (rowNum >= 0) {
            res = nonClassRows.get(rowNum);
        } else {
            res = null; // was not found
        }

        return res;
    }

    @Override
    public Row insertId(String inputId) {
        return null;
    }
}



class MethodSymbolTable extends SymbolTable {
    private ClassSymbolTable containerClass;
    private ArrayList<VarRow> varRows;

    public MethodSymbolTable(ClassSymbolTable containerClass) {
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
    public Row getIdIndex(String inputId) {
        return null;
    }

    @Override
    public Row insertId(String inputId) {
        return null;
    }
}



class MasterSymbolTable extends SymbolTable {
    private ArrayList<ClassRow> classRows;

    public ArrayList<ClassRow> getClassRows() {
        return classRows;
    }


    @Override
    public Row getIdIndex(String inputId) {
        return null;
    }

    @Override
    public Row insertId(String inputId) {
        return null;
    }
}



abstract class Row {
    private SymbolTable symbolTable;
    private String name;

//    private SymbolTable target; // only if is class or func;

//    private ArrayList<String> attributes; // ???


    public Row(SymbolTable symbolTable, String name) {
        this.symbolTable = symbolTable;
        this.name = name;
//        attributes = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

//    public ArrayList<String> getAttributes() {
//        return attributes;
//    }

//    public SymbolTable getTarget() {
//        return target;
//    }

//    public void setTarget(SymbolTable target) {
//        this.target = target;
//    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public boolean equals(Object obj) { // CONSIDERS ONLY NAME
        return (obj.getClass().equals(this.getClass())) &&
                (((Row)obj).getName().equals(this.getName())); // may consider type for supporting same names
    }

//    @Override
//    public String toString() {
////        return ("ID " + name + " in : " + symbolTable);
//        String res = "";
//        res += "Row: " + getName() + ", and is " + getType() + ". is in table : " + symbolTable.getName() + "\n";
//        if (target != null) {
//            res += "\t" + target.toString().replaceAll("\\n", "\n\t");
//        }
//        res += "\n";
//
//        return res;
//    }
}

class ClassRow extends Row {
    private ClassSymbolTable classSymbolTable;

    public ClassRow(SymbolTable symbolTable, String name, ClassSymbolTable classSymbolTable) {
        super(symbolTable, name);
        this.classSymbolTable = classSymbolTable;
    }
}

class NonClassRow extends Row {
    private Type type;
    private int address;

    public NonClassRow(SymbolTable symbolTable, String name) {
        super(symbolTable, name);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}

class MethodRow extends NonClassRow {
    private MethodSymbolTable methodSymbolTable;

    public MethodRow(SymbolTable symbolTable, String name, Type type, MethodSymbolTable methodSymbolTable) {
        super(symbolTable, name, type);
        this.methodSymbolTable = methodSymbolTable;
    }
}

class VarRow extends NonClassRow {
    public VarRow(SymbolTable symbolTable, String name, Type type) {
        super(symbolTable, name, type);
    }
}

enum Type {
    INT, BOOL
}