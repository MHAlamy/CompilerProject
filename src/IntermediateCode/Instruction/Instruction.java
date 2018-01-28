package IntermediateCode.Instruction;

import IntermediateCode.Instruction.InstructionParameter.InstructionParameter;

public class Instruction {

    private InstructionType type;
    private InstructionParameter parameters[];

    public Instruction(InstructionType type) {
        this.type = type;
    }

    public Instruction(InstructionType type,
                       InstructionParameter ip1,
                       InstructionParameter ip2,
                       InstructionParameter ip3) {
        this.type = type;
        parameters = new InstructionParameter[4]; // TODO: 1/28/18 siez?
        parameters[0] = ip1;
        parameters[1] = ip2;
        parameters[2] = ip3;
    }

    public InstructionType getType() {
        return type;
    }

    public void setIp(int index, InstructionParameter ip) {
        parameters[index] = ip;
    }

    @Override
    public String toString() {
        return "(" + type + ", " + parameters[0] + ", " + parameters[1] + ", " + parameters[2] + ")";
    }
}
