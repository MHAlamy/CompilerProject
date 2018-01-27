package IntermediateCode.ProgramBlock;

import IntermediateCode.Instruction.Instruction;

/**
 * Created by mha on 1/27/18.
 */
public class InstructionPBR extends ProgramBlockRow{

    private Instruction instruction;

    public InstructionPBR(Instruction instruction) {
        this.instruction = instruction;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }
}
