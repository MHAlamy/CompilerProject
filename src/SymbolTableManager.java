public class SymbolTableManager {

    private MasterSymbolTable masterSymbolTable;
    private ScopeState scopeState;
    private SymbolTable currentSymbolTable;

    public SymbolTableManager() {
        masterSymbolTable = new MasterSymbolTable("SymbolTableManager");
        scopeState = ScopeState.DEFAULT;
        currentSymbolTable = masterSymbolTable;

    }

    public Row findRow (String name) {
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

        switch (scopeState) {
            case DEFINE_CLASS:
                classSymbolTable = new ClassSymbolTable(name, masterSymbolTable);
                tmpRow = new ClassRow(masterSymbolTable, name, classSymbolTable);

                return masterSymbolTable.insertRow(tmpRow);

            case DEFINE_FIELD:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                return currentSymbolTable.insertRow(tmpRow);

            case DEFINE_METHOD:
                tmpRow = new MethodRow(currentSymbolTable, name); // ???
                return currentSymbolTable.insertRow(tmpRow);

            case DEFINE_VAR:
                tmpRow = new VarRow(currentSymbolTable, name); // ???
                return currentSymbolTable.insertRow(tmpRow);

            default: // USING UNDEFINED ID! ERROR!!!!

                return null;
        }
    }


    public Row getRowIndex(String curRead) {
        Row res;


        if (scopeState.equals(ScopeState.DEFINE_CLASS)) {
            Row foundRow = findRow(curRead);

            if (foundRow == null) { // ok, add new class
                foundRow = declareRow(curRead);
//                tmpClassRow.getTarget().setName(curRead); // DOES NEED NAME?
//                setCurSymbolTable(foundRow.getClassSymbolTable()); // ??????
                currentSymbolTable = ((ClassRow)foundRow).getClassSymbolTable();

//                res = new Token("id", new RowIndex(foundRow));
                res = foundRow;
                // TODO: 1/26/18 change table???
            } else { // error, return found class??
//                System.out.println("Error at line " +  + ". Class " + curRead +
                System.out.println("Error. Class " + curRead +
                        " was already defined. this input will be counted as old class'");
//                res = new Token("id", new RowIndex(foundRow));
                res = foundRow;
            }
        }
//        else if (scopeState.equals("extendsThis")) {
//            ClassRow foundRow = masterSymbolTable.getRow(curRead);
//
//            if (foundRow == null) { // error
//                System.out.println("Extended class " + curRead + " does not exist!");
//                res = null;
////                return new Token("id", foundRow);
//            } else { // return old defined class, set container of last one
//                if (curSymbolTable.getClass().equals(ClassSymbolTable.class))
//                    ((ClassSymbolTable)curSymbolTable).setParentClass(foundRow.getClassSymbolTable());
//
//                res = new Token("id", new RowIndex(foundRow));
//            }
//
//        }
        else {
            SymbolTable backupSymbolTable = currentSymbolTable;
            boolean wasFound = false;

            Row foundRow = null;

            while (currentSymbolTable != null) {
//                foundRow = tmpST.getRow(curRead);
                foundRow = findRow(curRead);
//                index = tmpST.getRow(curRead);


                if (foundRow != null) { // was found
                    wasFound = true;
                    break;
                } else {
                    try {
                        if (currentSymbolTable.getClass().equals(MethodSymbolTable.class)) {
                            currentSymbolTable = ((MethodSymbolTable) currentSymbolTable).getContainerClass();
                        } else { // it's class symbol table
                            currentSymbolTable = ((ClassSymbolTable) currentSymbolTable).getParentClass();
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
                            System.out.println("Variable/Function " + curRead + " is already in this scope." +
                                    " this declaration will be ignored");
                            // TODO: 1/26/18 what to do if ID is for function?

//                            res = new Token("id", new RowIndex(foundRow));
                            res = foundRow;
                        } else {
                            currentSymbolTable = backupSymbolTable;
                            res = declareRow(curRead);
                            // details about symbolRow can be set later?
                        }
                        break;

                    default:
                        res = foundRow;
                        System.out.println("ACCESSED " + curRead + ", inside " + foundRow.getContainer().getName() + "\n");
                        break;

                }
            } else { // add to curSymbolTable
                switch (scopeState) {
                    case DEFINE_FIELD:
                    case DEFINE_VAR:
                    case DEFINE_METHOD:
                        currentSymbolTable = backupSymbolTable;
                        res = declareRow(curRead);
                        break;

                    default:
                        System.out.println("Variable/Function " + curRead + " is not defined in this scope");
                        // TODO: 1/26/18 error handling??
                        res = null;
                        break;

                }

//                index = curSymbolTable.insertRow(curRead);
//                return new Token("id", index);
            }
            currentSymbolTable = backupSymbolTable;
        }
        scopeState = ScopeState.DEFAULT;

//        System.out.println("Symbol Table Result: ");
        System.out.println(masterSymbolTable + "\n\n");
        return res;
    }
    public void setScopeState(ScopeState scopeState) {
        this.scopeState = scopeState;
    }

    public ScopeState getScopeState() {
        return scopeState;
    }
}

enum ScopeState {
    DEFINE_CLASS, DEFINE_FIELD, DEFINE_METHOD, DEFINE_VAR, DEFAULT
}