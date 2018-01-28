package IntermediateCode.SemanticStack.Object;

import SymbolTable.Row.Row;

/**
 * Created by mha on 1/28/18.
 */
public class RowSSObject extends SSObject {

    private Row row;

    public RowSSObject(Row row) {
        super(SSType.ROW);
    }

    public Row getRow() {
        return row;
    }

}
