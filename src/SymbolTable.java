public class SymbolTable {
    SymbolTable container;

    public SymbolTable(SymbolTable container) {
        this.container = container;
    }
}
