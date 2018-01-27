import IntermediateCode.ProgramBlock.ProgramBlock;
import IntermediateCode.SemanticStack.SemanticStack;

public class IntermediateCodeGenerator {

    private ProgramBlock programBlock;
    private SemanticStack semanticStack;
    private Parser parser;

    public IntermediateCodeGenerator(Parser parser) {
        this.parser = parser;
        programBlock = new ProgramBlock();
        semanticStack = new SemanticStack();
    }



}
