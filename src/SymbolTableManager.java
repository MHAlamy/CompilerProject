/**
 * Created by mha on 1/27/18.
 */
public class SymbolTableManager {

    private MasterSymbolTable masterSymbolTable;
    private ScopeState scopeState;
    private SymbolTable currentSymbolTable;

    public Row findRow (String name) {

    }

    public Row declareRow (String name) {

    }

}

enum ScopeState {
    DEFINE_CLASS, DEFINE_FIELD, DEFINE_METHOD, DEFINE_VAR, DEFAULT
}