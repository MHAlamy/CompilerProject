import java.util.ArrayList;

public abstract class SymbolTable {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public abstract Row getRow(String name) throws Exception;

    public abstract Row insertRow(String name);

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

    private MasterSymbolTable masterSymbolTable;
    private ClassSymbolTable parentClass;
    private ArrayList<NonClassRow> nonClassRows;

    public ClassSymbolTable(MasterSymbolTable masterSymbolTable) {
        this.masterSymbolTable = masterSymbolTable;
        parentClass = null;
        nonClassRows = new ArrayList<>();
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
    public Row getRow(String name) {
        Row res;

        int rowNum = nonClassRows.indexOf(new NonClassRow(this, name));

        if (rowNum >= 0) {
            res = nonClassRows.get(rowNum);
        } else {
            res = null; // was not found
        }

        return res;
    }

    @Override
    public Row insertRow(String name) {
        return null;
    }
}



class MethodSymbolTable extends SymbolTable {

    private ClassSymbolTable containerClass;
    private ArrayList<VarRow> varRows;

    public MethodSymbolTable(ClassSymbolTable containerClass) {
        this.containerClass = containerClass;
        varRows = new ArrayList<>();
    }

    public ClassSymbolTable getContainerClass() {
        return containerClass;
    }

    public ArrayList<VarRow> getVarRows() {
        return varRows;
    }

    @Override
    public Row getRow(String name) throws Exception{
        VarRow res = null;
        for (VarRow varRow : varRows)
            if (varRow.getName().equals(name)) {
                res = varRow;
                break;
            }
        if (res == null)
            throw new Exception("Not Found");
        return res;
    }

    @Override
    public Row insertRow(String name) {
        VarRow temp = new VarRow(this, name)
    }
}



class MasterSymbolTable extends SymbolTable {

    private ArrayList<ClassRow> classRows;

    public MasterSymbolTable() {
        classRows = new ArrayList<>();
    }

    public ArrayList<ClassRow> getClassRows() {
        return classRows;
    }

    @Override
    public ClassRow getRow(String name) throws Exception {
        ClassRow res = null;
        for (ClassRow row : classRows) {
            if (row.getName().equals(name)) {
                res = row;
                break;
            }
        }
        if (res == null)
            throw new Exception("Not found");
        return res;
    }

    @Override
    public Row insertRow(String name) {
        ClassSymbolTable classSymbolTable = new ClassSymbolTable(this);
        ClassRow temp = new ClassRow(this, name, classSymbolTable);
        classRows.add(temp);
        return temp;
    }
}



abstract class Row {
    private SymbolTable container;
    private String name;

//    private SymbolTable target; // only if is class or func;

//    private ArrayList<String> attributes; // ???


    public Row(SymbolTable container, String name) {
        this.container = container;
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

    public SymbolTable getContainer() {
        return container;
    }

    @Override
    public boolean equals(Object obj) { // CONSIDERS ONLY NAME
        return (obj.getClass().equals(this.getClass())) &&
                (((Row)obj).getName().equals(this.getName())); // may consider type for supporting same names
    }

//    @Override
//    public String toString() {
////        return ("ID " + name + " in : " + container);
//        String res = "";
//        res += "Row: " + getName() + ", and is " + getType() + ". is in table : " + container.getName() + "\n";
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

    public ClassRow(SymbolTable container, String name, ClassSymbolTable classSymbolTable) {
        super(container, name);
        this.classSymbolTable = classSymbolTable;
    }
}

class NonClassRow extends Row {
    private Type type;
    private int address;

    public NonClassRow(SymbolTable container, String name, Type type) {
        super(container, name);
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

    public MethodRow(SymbolTable container, String name, Type type, MethodSymbolTable methodSymbolTable) {
        super(container, name, type);
        this.methodSymbolTable = methodSymbolTable;
    }
}

class VarRow extends NonClassRow {
    public VarRow(SymbolTable container, String name, Type type) {
        super(container, name, type);
    }
}

enum Type {
    INT, BOOL
}