package IntermediateCode.ProgramBlock;

import java.util.ArrayList;

public class ProgramBlock {

    private int currentRow;
    private int currentAllocRow;
    private ArrayList<ProgramBlockRow> rows;

    public ProgramBlock() {
        currentRow = 0;
        currentAllocRow = 500;
        rows = new ArrayList<>(1000);
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

    public int allocateInteger() {
        rows.set(currentAllocRow, new IntegerPBR(0));
        currentAllocRow++;
        return currentAllocRow - 1;
    }

    public int getCurrentRow() {
        return currentRow;
    }
}