import IntermediateCode.Instruction.Instruction;
import IntermediateCode.Instruction.InstructionParameter.*;
import IntermediateCode.Instruction.InstructionType;
import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.Object.*;
import IntermediateCode.SemanticStack.SemanticStack;
import SymbolTable.Row.MethodRow;
import SymbolTable.Row.Row;
import SymbolTable.Row.ClassRow;
import SymbolTable.Row.VarRow;
import SymbolTable.SymbolTable;
import SymbolTable.MethodSymbolTable;

public class IntermediateCodeGenerator {

    private ProgramBlock programBlock;
    private SemanticStack semanticStack;
    private Parser parser;
    private SymbolTableManager symbolTableManager;

    public IntermediateCodeGenerator(Parser parser, SemanticStack semanticStack, ProgramBlock programBlock) {
        this.parser = parser;
        this.programBlock = programBlock;
        this.semanticStack = semanticStack;
        symbolTableManager = parser.getSymbolTableManager();
    }

    public void setClassFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFINE_CLASS);
    }

    public void unsetClassFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFAULT);
    }

    public void setFieldFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFINE_FIELD);
    }

    public void unsetFieldFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFAULT);
    }

    public void setMethodFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFINE_METHOD);
    }

    public void unsetMethodFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFAULT);
    }

    public void setVarFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFINE_VAR);
    }

    public void unsetVarFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFAULT);
    }

    public void setParFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFINE_PAR);
    }

    public void unsetParFlag() {
        parser.getSymbolTableManager().setScopeState(ScopeState.DEFAULT);
    }

    public void getInScope() throws Exception {
        symbolTableManager.getInScope();
    }

    public void getOutOfScope() throws Exception {
        symbolTableManager.getOutOfScope();
    }

    public void createScopeEntry(Token nextToken) throws Exception {
        Row entry = null;
        if (nextToken.getIndex() instanceof RowIndex) {
            entry = ((RowIndex) nextToken.getIndex()).getRow();
        }
        else
            throw new Exception("entry row not found!");

        symbolTableManager.setScopeEntryRow(entry);
    }

    public void saveMainAddress() {
        Instruction temp = new Instruction(InstructionType.JP, new IntegerIP(programBlock.getCurrentRow()), null, null);
        programBlock.setInstructionAtRow(0, temp);
    }

    public void setParentClass(Token nextToken) throws Exception {
        ClassRow fatherClassRow = null;
        if (nextToken.getIndex() instanceof RowIndex) {
            fatherClassRow = (ClassRow) ((RowIndex) nextToken.getIndex()).getRow();
        } else
            throw new Exception("entry row not found!");

        ClassRow childClassRow = symbolTableManager.getLastClassDefinedRow();
        childClassRow.getClassSymbolTable().setParentClass(fatherClassRow.getClassSymbolTable());

//        System.out.println(childClassRow.getName() + " IS CHILD OF " + fatherClassRow.getName());
    }

    public void saveType(Token nextToken) throws Exception {
        String tokenName = nextToken.getName() ;
        if (tokenName.equals("boolean") || tokenName.equals("int"))
            semanticStack.push(new StringSSObject(tokenName));
        else
            throw new Exception("Token is not type");

//        System.out.println("IS SAVING " + semanticStack.size());
    }

    public void pushSimpleId(Token nextToken) throws Exception {
        int address;
        Index tempIndex = nextToken.getIndex();
        if (tempIndex instanceof RowIndex) {
            Row tempRow = ((RowIndex) tempIndex).getRow();
            if (tempRow instanceof VarRow) {
                address = ((VarRow) tempRow).getAddress();
            } else
                throw new Exception("Expected to see VarRow");
        } else
            throw new Exception("Expected to have row index");
        semanticStack.push(new AddressSSObject(address));
//        System.out.println("size after push = " + semanticStack.size());
    }

    public void pushInteger(Token nextToken) throws Exception {
        int value;
        Index tempIndex = nextToken.getIndex();
        if (tempIndex instanceof ValueIndex) {
            value = ((ValueIndex)tempIndex).getValue();
        } else
            throw new Exception("Expected to see value index");

        semanticStack.push(new IntegerSSObject(value));
    }

    public void pushBoolean(Token nextToken) throws Exception {
        int val;
        if (nextToken.getName().equals("true")) {
            val = 1;
        } else if (nextToken.getName().equals("false")) {
            val = 0;
        } else {
            throw new Exception("Not a Boolean value");
        }

        semanticStack.push(new BooleanSSObject(val));
    }

    public void add() throws Exception {
//        System.out.println("addADDING " + semanticStack.size());
        Instruction instruction = new Instruction(InstructionType.ADD);

        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            instruction.setIp(0, new InstructionParameter(ParameterType.INTEGER, value));
        } else if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            instruction.setIp(0, new InstructionParameter(ParameterType.ADDRESS, value));
        } else
            throw new Exception("Expected to see address or integer");

        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            instruction.setIp(1, new InstructionParameter(ParameterType.INTEGER, value));
        } else if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            instruction.setIp(1, new InstructionParameter(ParameterType.ADDRESS, value));
        } else
            throw new Exception("Expected to see address or integer");

        int destination = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(destination));

        instruction.setIp(2, new InstructionParameter(ParameterType.ADDRESS, destination));

        programBlock.addInstruction(instruction);
    }

    public void assign() throws Exception {
        Instruction instruction = new Instruction(InstructionType.ASSIGN);

//        System.out.println("assigning: " + semanticStack.size());
        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            instruction.setIp(0, new InstructionParameter(ParameterType.INTEGER, value));

        } else if (semanticStack.peek() instanceof BooleanSSObject) {
            int value = ((BooleanSSObject)semanticStack.pop()).getValue();
            if (value == 0)
                instruction.setIp(0, new InstructionParameter(ParameterType.BOOLEAN, 0));
            else
                instruction.setIp(0, new InstructionParameter(ParameterType.BOOLEAN, 1));
            // TODO: 1/28/18 check what is going on
        } else if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            instruction.setIp(0, new InstructionParameter(ParameterType.ADDRESS, value));

        } else
            throw new Exception("Expected to see address or integer");

        if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            instruction.setIp(1, new InstructionParameter(ParameterType.ADDRESS, value));
        } else
            throw new Exception("Expected to see address");

        programBlock.addInstruction(instruction);
    }

    public void whileSaveHere() {
        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
    }

    public void whileReserveHere() {
        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
        programBlock.incrementCurrentRow();
    }

    public void whileFill() {
        Instruction jpf = new Instruction(InstructionType.JPF);
        int pbRow = ((AddressSSObject)semanticStack.pop()).getValue();
        jpf.setIp(0, getIPFromSS());
        jpf.setIp(1, new InstructionParameter(ParameterType.ADDRESS, programBlock.getCurrentRow()+1));
        programBlock.setInstructionAtRow(pbRow, jpf);

        Instruction jp = new Instruction(InstructionType.JP);
        jp.setIp(0, getIPFromSS());
        programBlock.addInstruction(jp);
    }

    public void ifReserveHere() {
        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
        programBlock.incrementCurrentRow();
    }

    public void ifFillJpf() {
        Instruction jpf = new Instruction(InstructionType.JPF);
        int pbRow = ((AddressSSObject)semanticStack.pop()).getValue();
        jpf.setIp(0, getIPFromSS());
        jpf.setIp(1, new InstructionParameter(ParameterType.ADDRESS, programBlock.getCurrentRow()+1));
        programBlock.setInstructionAtRow(pbRow, jpf);

        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
        programBlock.incrementCurrentRow();
    }

    public void ifFillJp() {
        Instruction jp = new Instruction(InstructionType.JP);
        int pbRow = ((AddressSSObject)semanticStack.pop()).getValue();
        jp.setIp(0, new InstructionParameter(ParameterType.ADDRESS, programBlock.getCurrentRow()));

        programBlock.setInstructionAtRow(pbRow, jp);
    }

    public void forSaveHere() {
        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
    }

    public void forReserveHere() {
        semanticStack.push(new AddressSSObject(programBlock.getCurrentRow()));
        programBlock.incrementCurrentRow();
    }

    public void forStep() throws Exception {
        add();
        assign();
    }

    public void forFill() {
        Instruction jpf = new Instruction(InstructionType.JPF);
        int pbRow = ((AddressSSObject)semanticStack.pop()).getValue();
        jpf.setIp(0, getIPFromSS());
        jpf.setIp(1, new AddressIP(programBlock.getCurrentRow() + 1));

        programBlock.setInstructionAtRow(pbRow, jpf);

        Instruction jp = new Instruction(InstructionType.JP);
        int destination = ((AddressSSObject)semanticStack.pop()).getValue();
        jp.setIp(0, new AddressIP(destination));

        programBlock.addInstruction(jp);
    }

    public void print() {
        Instruction instruction = new Instruction(InstructionType.PRINT);
        instruction.setIp(0, getIPFromSS());
        programBlock.addInstruction(instruction);
    }

    public void isEqual() {
        Instruction instruction = new Instruction(InstructionType.EQ);
        instruction.setIp(0, getIPFromSS());
        instruction.setIp(1, getIPFromSS());
        int address = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(address));
        instruction.setIp(2, new AddressIP(address));
        programBlock.addInstruction(instruction);
    }

    public void isLess() {
        Instruction instruction = new Instruction(InstructionType.LT);
        instruction.setIp(1, getIPFromSS());
        instruction.setIp(0, getIPFromSS());
        int address = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(address));
        instruction.setIp(2, new AddressIP(address));
        programBlock.addInstruction(instruction);
    }

    public void sub() {
        Instruction instruction = new Instruction(InstructionType.SUB);
        instruction.setIp(1, getIPFromSS());
        instruction.setIp(0, getIPFromSS());
        int address = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(address));
        instruction.setIp(2, new AddressIP(address));
        programBlock.addInstruction(instruction);
    }

    public void mult() {
        Instruction instruction = new Instruction(InstructionType.MULT);
        instruction.setIp(0, getIPFromSS());
        instruction.setIp(1, getIPFromSS());
        int address = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(address));
        instruction.setIp(2, new AddressIP(address));
        programBlock.addInstruction(instruction);
    }

    public void and() {
        Instruction instruction = new Instruction(InstructionType.AND);
        instruction.setIp(0, getIPFromSS());
        instruction.setIp(1, getIPFromSS());
        int address = programBlock.allocateInteger();
        semanticStack.push(new AddressSSObject(address));
        instruction.setIp(2, new AddressIP(address));
        programBlock.addInstruction(instruction);
    }

    public void pushId1(Token nextToken) throws Exception {
        Index tempIndex = nextToken.getIndex();
        Row tempRow = ((RowIndex)tempIndex).getRow();
        if (tempRow instanceof VarRow) {
            pushSimpleId(nextToken);
        } else {
            ClassRow classRow = (ClassRow) tempRow;
            symbolTableManager.setBackupSymbolTable(symbolTableManager.getCurrentSymbolTable());
            symbolTableManager.setCurrentSymbolTable(classRow.getClassSymbolTable());
        }
    }

    public void pushId2(Token nextToken) throws Exception {
        Index tempIndex = nextToken.getIndex();
        Row tempRow = ((RowIndex)tempIndex).getRow();

        symbolTableManager.setCurrentSymbolTable(symbolTableManager.getBackupSymbolTable());

        if (tempRow instanceof VarRow) {
            pushSimpleId(nextToken);
        } else {
            MethodRow methodRow = ((MethodRow) tempRow);
             Instruction assign = new Instruction(InstructionType.ASSIGN);
             assign.setIp(0, new IntegerIP(programBlock.getCurrentRow() + 2));
             assign.setIp(1, new AddressIP(methodRow.getReturnJumpAddress()));

             programBlock.addInstruction(assign);

             Instruction jump = new Instruction(InstructionType.JP);
             assign.setIp(0, new AddressIP(methodRow.getAddress()));

             programBlock.addInstruction(jump);

        }
    }

    public void returnFromMethod() {
        MethodSymbolTable mst = ((MethodSymbolTable)symbolTableManager.getCurrentSymbolTable());
        int returnAddress = mst.getParentRow().getReturnJumpAddress();

        Instruction jp = new Instruction(InstructionType.JP);
        jp.setIp(0, new IndirectIP(returnAddress));
        programBlock.addInstruction(jp);
    }

    private InstructionParameter getIPFromSS() {
        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.INTEGER, value);
        } else if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.ADDRESS, value);
        } else if (semanticStack.peek() instanceof BooleanSSObject) {
            int value = ((BooleanSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.BOOLEAN, value);
        }

        return null;
    }

//    public void pushRow1(Token nextToken) throws Exception {
//        Index tempIndex = nextToken.getIndex();
//        if (tempIndex instanceof RowIndex) {
//            Row tempRow = ((RowIndex) tempIndex).getRow();
//            if (tempRow instanceof VarRow) {
//                int address = ((VarRow) tempRow).getAddress();
//                semanticStack.push(new AddressSSObject(address));
//            } else if (tempRow instanceof ClassRow) {
//                ClassRow tempClassRow = (ClassRow) tempRow;
//                semanticStack.push(new RowSSObject(tempClassRow));
//            } else
//                throw new Exception("Expected to see VarRow");
//        } else
//            throw new Exception("Expected to have row index");
//    }
//
//    public void pushRow2(Token nextToken) {
//        SymbolTable initialSymbolTable = symbolTableManager.getCurrentSymbolTable();
//        if (semanticStack.peek() instanceof RowSSObject) {
//            Row tempRow = ((RowSSObject)semanticStack.pop()).getRow();
//            if (tempRow instanceof ClassRow) {
//                ClassRow targetClassRow = (ClassRow) tempRow;
//                symbolTableManager.setCurrentSymbolTable(targetClassRow.getClassSymbolTable());
//            }
//        }
//
//    }

}
