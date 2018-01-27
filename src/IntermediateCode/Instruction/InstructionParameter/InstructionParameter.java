package IntermediateCode.Instruction.InstructionParameter;

import IntermediateCode.Instruction.ParameterType;

public class InstructionParameter {

    private ParameterType type;
    private int value;

    public InstructionParameter(ParameterType type, int value) {
        this.type = type;
        this.value = value;
    }

    public ParameterType getType() {
        return type;
    }

    public void setType(ParameterType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String res = "";
        if (type == ParameterType.ADDRESS)
            res += value;
        else if (type == ParameterType.INTEGER)
            res += "#" + value;
        else if (type == ParameterType.INDIRECT)
            res += "@" + value;
        else if (type == ParameterType.BOOLEAN) {
            if (value == 0)
                res += "FALSE";
            else
                res += "TRUE";
        }
        return res;
    }
}

enum ParameterType {
    ADDRESS, INDIRECT, INTEGER, BOOLEAN
}
