package IntermediateCode.Instruction.InstructionParameter;

import IntermediateCode.Instruction.InstructionParameter.InstructionParameter;
import IntermediateCode.Instruction.ParameterType;

/**
 * Created by mha on 1/27/18.
 */
public class IntegerIP extends InstructionParameter {

    public IntegerIP(int value) {
        super(ParameterType.INTEGER, value);
    }

}
