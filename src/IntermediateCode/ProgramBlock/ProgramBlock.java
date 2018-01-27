package IntermediateCode.ProgramBlock;

import IntermediateCode.Instruction.Instruction;

import java.util.ArrayList;

public class ProgramBlock {

    private int currentRow;
    private int currentAllocRow;
    private ArrayList<ProgramBlockRow> rows;

    public ProgramBlock() {
        currentRow = 1;
        currentAllocRow = 500;
        rows = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            rows.add(null);
        }
    }

    public int getValueAtRow(int rowNumber) throws Exception {
        if (rows.get(rowNumber) instanceof InstructionPBR)
            throw new Exception("Wrong PBR type.");
        return ((IntegerPBR)rows.get(rowNumber)).getValue();
    }

    public void setValueAtRow(int rowNumber, int value) throws Exception {
        if (rows.get(rowNumber) instanceof InstructionPBR)
            throw new Exception("Wrong PBR type.");
        ((IntegerPBR)rows.get(rowNumber)).setValue(value);
    }

    public void setInstructionAtRow(int rowNumber, Instruction instruction) {
//        ((InstructionPBR)rows.get(rowNumber)).setInstruction(instruction);
        rows.set(rowNumber, new InstructionPBR(instruction));
    }

    public int allocateInteger() {
        rows.set(currentAllocRow, new IntegerPBR(0));
        currentAllocRow++;
        return currentAllocRow - 1;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            if (rows.get(i) != null)
                if (rows.get(i) instanceof InstructionPBR)
                    res.append(i).append(" ").append(((InstructionPBR) rows.get(i)).getInstruction()).append("\n");
                else
                    res.append(i).append(" ").append(((IntegerPBR) rows.get(i)).getValue()).append("\n");
        }
        return res.toString();
    }
}
