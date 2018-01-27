import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.SemanticStack;
import SymbolTable.Row.Row;

public class IntermediateCodeGenerator {

    private ProgramBlock programBlock;
    private SemanticStack semanticStack;
    private Parser parser;
    private SymbolTableManager symbolTableManager;

    public IntermediateCodeGenerator(Parser parser) {
        this.parser = parser;
        programBlock = new ProgramBlock();
        semanticStack = new SemanticStack();
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

    public void getInScope() throws Exception {
        symbolTableManager.getInScope();
    }

    public void getOutOfScope() throws Exception {
        symbolTableManager.getOutOfScope();
    }

    public void createScopeEntry(Token nextToken) throws Exception {
        Row entry = null;
        if (nextToken.getIndex() instanceof RowIndex)
            entry = ((RowIndex) nextToken.getIndex()).getRow();
        else
            throw new Exception("entry row not found!");
        symbolTableManager.setScopeEntryRow(entry);
    }

}
