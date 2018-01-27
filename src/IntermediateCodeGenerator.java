import IntermediateCode.Instruction.Instruction;
import IntermediateCode.Instruction.InstructionParameter.IntegerIP;
import IntermediateCode.Instruction.InstructionType;
import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.Object.StringSSObject;
import IntermediateCode.SemanticStack.SemanticStack;
import SymbolTable.Row.Row;
import SymbolTable.Row.ClassRow;

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
    }

}
