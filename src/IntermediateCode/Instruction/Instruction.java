package IntermediateCode.Instruction;

import java.util.Arrays;

/**
 * Created by mha on 1/27/18.
 */
public class Instruction {

    private InstructionType type;
    private InstructionParameter parameters[];

    public Instruction(InstructionType type,
                       InstructionParameter ip1,
                       InstructionParameter ip2,
                       InstructionParameter ip3) {
        this.type = type;
        parameters = new InstructionParameter[3];
        parameters[1] = ip1;
        parameters[2] = ip2;
        parameters[3] = ip3;
    }

    public InstructionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return null;
    }
}

enum InstructionType {
    ADD, SUB, AND, ASSIGN, EQ, JPF, JP, LT, MULT, NOT, PRINT
}
