package IntermediateCode.ProgramBlock;

/**
 * Created by mha on 1/27/18.
 */
public class PBInteger extends ProgramBlockRow {

    private int value;

    public PBInteger(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
