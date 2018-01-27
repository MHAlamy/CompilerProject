package IntermediateCode.Instruction;

import IntermediateCode.Instruction.InstructionParameter.InstructionParameter;

public class Instruction {

    private InstructionType type;
    private InstructionParameter parameters[];

    public Instruction(InstructionType type,
                       InstructionParameter ip1,
                       InstructionParameter ip2,
                       InstructionParameter ip3) {
        this.type = type;
        parameters = new InstructionParameter[4]; // TODO: 1/28/18 siez?
        parameters[1] = ip1;
        parameters[2] = ip2;
        parameters[3] = ip3;
    }

    public InstructionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "(" + type + ", " + parameters[0] + ", " + parameters[1] + ", " + parameters[2] + ")";
    }
}
