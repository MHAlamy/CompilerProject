package IntermediateCode.ProgramBlock;

import IntermediateCode.Instruction.Instruction;

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
