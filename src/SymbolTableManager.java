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


    public RowIndex getRowIndex(String curRead) {
        SymbolTable tmpST = curSymbolTable;
        Token res;

        if (scopeState.equals(ScopeState.DEFINE_CLASS)) {
            ClassRow foundRow = (ClassRow)findRow(curRead);


            if (foundRow == null) { // ok, add new class
                foundRow = (ClassRow)masterSymbolTable.insertRow(curRead);

//                tmpClassRow.getTarget().setName(curRead); // DOES NEED NAME?
                setCurSymbolTable(foundRow.getClassSymbolTable());

                res = new Token("id", new RowIndex(foundRow));
                // TODO: 1/26/18 change table???
            } else { // error, return found class??
                System.out.println("Error at line " + curLine + ". Class " + curRead +
                        " was already defined. this input will be counted as old class'");
                res = new Token("id", new RowIndex(foundRow));
            }
        } else if (scopeState.equals("extendsThis")) {
            ClassRow foundRow = masterSymbolTable.getRow(curRead);

            if (foundRow == null) { // error
                System.out.println("Extended class " + curRead + " does not exist!");
                res = null;
//                return new Token("id", foundRow);
            } else { // return old defined class, set container of last one
                if (curSymbolTable.getClass().equals(ClassSymbolTable.class))
                    ((ClassSymbolTable)curSymbolTable).setParentClass(foundRow.getClassSymbolTable());

                res = new Token("id", new RowIndex(foundRow));
            }

        } else {
            boolean wasFound = false;

            Row foundRow = null;

            while (tmpST != null) {
                foundRow = tmpST.getRow(curRead);
//                index = tmpST.getRow(curRead);

                if (foundRow != null) { // was found
                    wasFound = true;
                    break;
                } else {
                    try {
                        if (tmpST.getClass().equals(MethodSymbolTable.class)) {
                            tmpST = ((MethodSymbolTable) tmpST).getContainerClass();
                        } else { // it's class symbol table
                            tmpST = ((ClassSymbolTable) tmpST).getParentClass();
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

                        break;
                    case DEFINE_METHOD:

                        break;
                    default:

                        break;
                }
                if (scopeState.equals("normalDef")) {
                    if (tmpST.equals(curSymbolTable)) {
                        System.out.println("Variable/Function " + curRead + " is already in this scope." +
                                " this declaration will be ignored");
                        // TODO: 1/26/18 what to do if ID is for function?

                        res = new Token("id", new RowIndex(foundRow));

                    } else { // declare in current symbol table
                        Index insertIndex;
                        Row insertRow = curSymbolTable.insertRow(curRead);
                        insertRow = curSymbolTable.insertRow(curRead);

                        // details about symbolRow can be set later?

//                        Row tmpSR = foundIndex.getRowPointer();

//                        tmpSR.setType("class");
//                        tmpSR.setTarget(new SymbolTable(null)); // container will be set if there is extends afterward
//                        tmpSR.getTarget().setClass(true);
//                        setCurSymbolTable(tmpSR.getTarget());

                        res =  new Token("id", new RowIndex(insertRow));
                    }
                } else {
                    res = new Token("id", new RowIndex(foundRow));
                    System.out.println("ACCESSED " + curRead + ", inside " + foundRow.getContainer().getName() + "\n");
                }
                // if state is useID or variable was already defined:


            } else { // add to curSymbolTable
                if (scopeState.equals("normalDef")) {
                    Index insertIndex;
                    Row insertRow;
                    insertRow = curSymbolTable.insertRow(curRead);

                    // details about symbolRow can be set later?

                    res = new Token("id", new RowIndex(insertRow));
                } else { // useID
                    System.out.println("Variable/Function " + curRead + " is not defined in this scope");
                    // TODO: 1/26/18 error handling??
                    res = new Token("id", null);
                }

//                index = curSymbolTable.insertRow(curRead);
//                return new Token("id", index);
            }
        }

        scopeState = "";

//        System.out.println("Symbol Table Result: ");
        System.out.println(masterSymbolTable + "\n\n");
        return res;
    }
    public void setScopeState(ScopeState scopeState) {
        this.scopeState = scopeState;
    }
}

enum ScopeState {
    DEFINE_CLASS, DEFINE_FIELD, DEFINE_METHOD, DEFINE_VAR, DEFAULT
}