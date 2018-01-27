import IntermediateCode.Instruction.Instruction;
import IntermediateCode.Instruction.InstructionParameter.IntegerIP;
import IntermediateCode.Instruction.InstructionType;
import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.SemanticStack;
import SymbolTable.Row.Row;
import SymbolTable.Row.ClassRow;

public class IntermediateCodeGenerator {

    private ProgramBlock programBlock;
    private SemanticStack semanticStack;
    private Parser parser;
    private SymbolTableManager symbolTableManager;

    private ClassRow lastClassDefinedRow;

    public IntermediateCodeGenerator(Parser parser) {
        this.parser = parser;
        programBlock = new ProgramBlock();
        semanticStack = new SemanticStack();
        symbolTableManager = parser.getSymbolTableManager();
    }

    public void setClassFlag() {
        System.out.println("jfiesoogejiejof");
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

    public void getInScope() throws Exception {
//        System.out.println("GETINSIDE");
        symbolTableManager.getInScope();
    }

    public void getOutOfScope() throws Exception {
        symbolTableManager.getOutOfScope();
    }

    public void createScopeEntry(Token nextToken) throws Exception {
        Row entry = null;
        if (nextToken.getIndex() instanceof RowIndex) {
            entry = ((RowIndex) nextToken.getIndex()).getRow();
            if (entry instanceof ClassRow)
                lastClassDefinedRow = (ClassRow) entry;
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

        ClassRow childClassRow = lastClassDefinedRow;
        childClassRow.getClassSymbolTable().setParentClass(fatherClassRow.getClassSymbolTable());
    }

}
