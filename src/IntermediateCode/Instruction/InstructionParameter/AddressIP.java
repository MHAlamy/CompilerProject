package IntermediateCode.Instruction.InstructionParameter;

import IntermediateCode.Instruction.InstructionParameter.InstructionParameter;
import IntermediateCode.Instruction.ParameterType;

/**
 * Created by mha on 1/27/18.
 */

public class AddressIP extends InstructionParameter {

    public AddressIP(int value) {
        super(ParameterType.ADDRESS, value);
    }

}
