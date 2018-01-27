import IntermediateCode.ProgramBlock.ProgramBlock;
import SymbolTable.*;
import SymbolTable.Row.*;

public class SymbolTableManager {

    private MasterSymbolTable masterSymbolTable;
    private ScopeState scopeState;
    private SymbolTable currentSymbolTable;
    private ProgramBlock programBlock;

    private Row scopeEntryRow;

    public SymbolTableManager() {
        masterSymbolTable = new MasterSymbolTable("SymbolTableManager");
        scopeState = ScopeState.DEFAULT;
        currentSymbolTable = masterSymbolTable;

    }

    //TODO: set STM program block

    public ProgramBlock getProgramBlock() {
        return programBlock;
    }

    public void setProgramBlock(ProgramBlock programBlock) {
        this.programBlock = programBlock;
    }

    public Row findRowInCurrentSymbolTable(String name) {
        Row tmpRow;
        ClassSymbolTable classSymbolTable;

        switch (scopeState) {
            case DEFINE_CLASS:
                classSymbolTable = new ClassSymbolTable(name, masterSymbolTable);
                tmpRow = new ClassRow(masterSymbolTable, name, classSymbolTable);

                return masterSymbolTable.getRow(tmpRow);

            case DEFINE_FIELD:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                return currentSymbolTable.getRow(tmpRow);

            case DEFINE_METHOD:
                tmpRow = new MethodRow(currentSymbolTable, name); // ???
                return currentSymbolTable.getRow(tmpRow);

            case DEFINE_VAR:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                return currentSymbolTable.getRow(tmpRow);

            case DEFAULT:
                classSymbolTable = new ClassSymbolTable(name, masterSymbolTable);
                tmpRow = new ClassRow(masterSymbolTable, name, classSymbolTable);
                ClassRow firstFindRes = masterSymbolTable.getRow(tmpRow);

                if (firstFindRes != null) {
                    return firstFindRes;
                } else {
                    tmpRow = new NonClassRow(currentSymbolTable, name);
                    return currentSymbolTable.getRow(tmpRow);
                }
            default:
                return null;
        }
    }

    public Row declareRow (String name) {
        Row tmpRow;
        ClassSymbolTable classSymbolTable;
        int address;

        switch (scopeState) {
            case DEFINE_CLASS:
                classSymbolTable = new ClassSymbolTable(name, masterSymbolTable);
                tmpRow = new ClassRow(masterSymbolTable, name, classSymbolTable);

                return masterSymbolTable.insertRow(tmpRow);

            case DEFINE_FIELD:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                address = programBlock.allocateInteger();
                ((VarRow)tmpRow).setAddress(address);
                return currentSymbolTable.insertRow(tmpRow);

            case DEFINE_METHOD:
                tmpRow = new MethodRow(currentSymbolTable, name); // ???
                address = programBlock.getCurrentRow();
                ((MethodRow)tmpRow).setAddress(address);
                return currentSymbolTable.insertRow(tmpRow);

            case DEFINE_VAR:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                address = programBlock.allocateInteger();
                ((VarRow)tmpRow).setAddress(address);
                return currentSymbolTable.insertRow(tmpRow);

            default: // USING UNDEFINED ID! ERROR!!!!

                return null;
        }
    }


    public Row getRowIndex(String name) {
        Row res;

        if (scopeState.equals(ScopeState.DEFINE_CLASS)) {
            Row foundRow = findRowInCurrentSymbolTable(name);

            if (foundRow == null) { // ok, add new class
                foundRow = declareRow(name);
                //TODO: scope in here
//                currentSymbolTable = ((ClassRow)foundRow).getClassSymbolTable();
                res = foundRow;
            } else { // error, return found class??
                //TODO: extension is in define_class state or not
                System.out.println("Error. Class " + name +
                        " was already defined. this input will be counted as old class'");
                res = foundRow;
            }
        }

        else {
            SymbolTable backupSymbolTable = currentSymbolTable;
            boolean wasFound = false;

            Row foundRow = null;

            while (currentSymbolTable != null) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("efjis"
                    );
                }
                foundRow = findRowInCurrentSymbolTable(name);

                if (foundRow != null) { // was found
                    wasFound = true;
                    break;
                } else {
                    try {
                        if (currentSymbolTable instanceof MethodSymbolTable) {
                            currentSymbolTable = ((MethodSymbolTable) currentSymbolTable)
                                    .getContainerClass();
                        } else { // it's class symbol table
                            currentSymbolTable = ((ClassSymbolTable) currentSymbolTable)
                                    .getParentClass();
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if (wasFound) {
                switch (scopeState) {
                    case DEFINE_FIELD:
                    case DEFINE_VAR:
                    case DEFINE_METHOD:
                        if (currentSymbolTable.equals(backupSymbolTable)) {
                            System.out.println("Variable/Function " + name + " is already in this scope." +
                                    " this declaration will be ignored");
                            // TODO: 1/26/18 what to do if ID is for function?

                            res = foundRow;
                        } else {
                            currentSymbolTable = backupSymbolTable;
                            res = declareRow(name);
                            // details about symbolRow can be set later?
                        }
                        break;

                    default:
                        res = foundRow;
                        System.out.println("ACCESSED " + name + ", inside " + foundRow.getContainer().getName() + "\n");
                        break;

                }
            } else { // add to curSymbolTable
                switch (scopeState) {
                    case DEFINE_FIELD:
                    case DEFINE_VAR:
                    case DEFINE_METHOD:
                        currentSymbolTable = backupSymbolTable;
                        res = declareRow(name);
                        break;

                    default:
                        System.out.println("Variable/Function " + name + " is not defined in this scope");
                        // TODO: 1/26/18 error handling??
                        res = null;
                        break;

                }

            }
            currentSymbolTable = backupSymbolTable;
        }
        scopeState = ScopeState.DEFAULT;

        System.out.println(masterSymbolTable + "\n\n");
        return res;
    }

    public void setScopeState(ScopeState scopeState) {
        this.scopeState = scopeState;
    }

    public ScopeState getScopeState() {
        return scopeState;
    }

    public void getInScope() throws Exception {
        SymbolTable destination = null;
        if (scopeEntryRow instanceof ClassRow)
            destination = ((ClassRow) scopeEntryRow).getClassSymbolTable();
        else if (scopeEntryRow instanceof MethodRow)
            destination = ((MethodRow) scopeEntryRow).getMethodSymbolTable();
        else
            throw new Exception("Scope Entry corrupted");

        currentSymbolTable = destination;
    }

    public void getOutOfScope() throws Exception {
        SymbolTable destination = null;
        if (currentSymbolTable instanceof ClassSymbolTable)
            destination = masterSymbolTable;
        else if (currentSymbolTable instanceof MethodSymbolTable)
            destination = ((MethodSymbolTable) currentSymbolTable).getContainerClass();
        else
            throw new Exception("Trying to get out of master");
        currentSymbolTable = destination;
    }

    public Row getScopeEntryRow() {
        return scopeEntryRow;
    }

    public void setScopeEntryRow(Row scopeEntryRow) {
        this.scopeEntryRow = scopeEntryRow;
    }
}

enum ScopeState {
    DEFINE_CLASS, DEFINE_FIELD, DEFINE_METHOD, DEFINE_VAR, DEFAULT
}