import java.util.ArrayList;

enum Type {
    INT, BOOL
}

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
//        if (obj.getClass().equals(SymbolTable.class)) {
        if (SymbolTable.class.isAssignableFrom(obj.getClass())) {
            SymbolTable tmp = (SymbolTable) obj;
            return this.name.equals(tmp.getName());
        }
        return false;
    }
}

class ClassSymbolTable extends SymbolTable {

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

//        int rowNum = nonClassRows.indexOf(new NonClassRow(this, name));
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
            System.out.println("ERROR! Row inserted inside ClassSymbolTable is INVALID");

        return (NonClassRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("ClassSymbolTable : " + getName() + ". Parent is = " +
                ((parentClass == null) ? ("NULL") : (parentClass.getName())) +
                "\n-----------------------------------------------\n");
        for (Row row :
                nonClassRows) {
            res += row;
        }
        return res;
    }
}

class MethodSymbolTable extends SymbolTable {

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
//        int rowNum = nonClassRows.indexOf(new NonClassRow(this, name));
        int rowNum = varRows.indexOf(idRow);

        if (rowNum >= 0)
            res = varRows.get(rowNum);
        else
            res = null; // was not found

        return res;
    }

    @Override
    public VarRow insertRow(Row idRow) {
//        VarRow temp = new VarRow(this, name);
        varRows.add((VarRow)idRow);
        return (VarRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("MethodSymbolTable : " + getName() + ". Container is = " +
                ((containerClass == null) ? ("NULL") : (containerClass.getName())) + "\n-----------------------------------------------\n");
        for (Row row :
                varRows) {
            res += row;
        }
        return res;
    }
}

class MasterSymbolTable extends SymbolTable {

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
//        int rowNum = nonClassRows.indexOf(new NonClassRow(this, name));
        int rowNum = classRows.indexOf(idRow);

        if (rowNum >= 0)
            res = classRows.get(rowNum);
        else
            res = null; // was not found

        return res;
    }

    @Override
    public ClassRow insertRow(Row idRow) {
//        ClassSymbolTable classSymbolTable = new ClassSymbolTable(name, this);
//        ClassRow temp = new ClassRow(this, name, classSymbolTable);
        classRows.add((ClassRow)idRow);
        return (ClassRow)idRow;
    }

    @Override
    public String toString() {
        String res = "";
        res += ("MasterSymbolTable : " + getName() + "\n-----------------------------------------------\n");
        for (Row row :
                classRows) {
            res += row;
        }
        return res;
    }
}

abstract class Row {
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

class ClassRow extends Row {
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
        res += "ClassRow: " + getName() + ". is in table : " + getContainer().getName() + "\n";
        if (classSymbolTable != null) {
            res += "\t" + classSymbolTable.toString().replaceAll("\\n", "\n\t");
        }
        res += "\n";

        return res;
    }
}

class NonClassRow extends Row {
    private Type type;
    private int address;

    public NonClassRow(SymbolTable container, String name) {
        super(container, name);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    @Override
    public String toString() {
        String res = "";
        res += "NonClassRow: " + getName() + ", and has type " + type + ". is in table : " + getContainer().getName() + "\n";

        return res;
    }
}

class MethodRow extends NonClassRow {
    private MethodSymbolTable methodSymbolTable;

    public MethodRow(SymbolTable container, String name) {//, MethodSymbolTable methodSymbolTable) {
        super(container, name);
//        this.methodSymbolTable = methodSymbolTable;
    }

    @Override
    public String toString() {
        String res = "";
        res += "MethodRow: " + getName() + ", and has type " + getType() + ". is in table : " + getContainer().getName() + "\n";
        if (methodSymbolTable != null) {
            res += "\t" + methodSymbolTable.toString().replaceAll("\\n", "\n\t");
        }
        res += "\n";

        return res;
    }
}

class VarRow extends NonClassRow {
    public VarRow(SymbolTable container, String name) {
        super(container, name);
    }

    @Override
    public String toString() {
        String res = "";
        res += "VarRow: " + getName() + ", and has type " + getType() + ". is in table : " + getContainer().getName() + "\n";

        return res;
    }
}