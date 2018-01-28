import IntermediateCode.Instruction.Instruction;
import IntermediateCode.Instruction.InstructionParameter.InstructionParameter;
import IntermediateCode.Instruction.InstructionParameter.IntegerIP;
import IntermediateCode.Instruction.InstructionParameter.ParameterType;
import IntermediateCode.Instruction.InstructionType;
import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.Object.*;
import IntermediateCode.SemanticStack.SemanticStack;
import SymbolTable.Row.Row;
import SymbolTable.Row.ClassRow;
import SymbolTable.Row.VarRow;
import SymbolTable.SymbolTable;

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
        System.out.println("size after push = " + semanticStack.size());
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

    public void add() throws Exception {
        System.out.println("addADDING " + semanticStack.size());
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

        System.out.println("assigning: " + semanticStack.size());
        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            instruction.setIp(0, new InstructionParameter(ParameterType.INTEGER, value));

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

    private InstructionParameter getIPFromSS() {
        if (semanticStack.peek() instanceof IntegerSSObject) {
            int value = ((IntegerSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.INTEGER, value);
        }
        if (semanticStack.peek() instanceof AddressSSObject) {
            int value = ((AddressSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.ADDRESS, value);
        }
        if (semanticStack.peek() instanceof BooleanSSObject) {
            int value = ((BooleanSSObject)semanticStack.pop()).getValue();
            return new InstructionParameter(ParameterType.BOOLEAN, value);
        }
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
