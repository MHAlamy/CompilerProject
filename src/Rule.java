import java.util.ArrayList;

public class Rule {
    private Term lhs;
    private ArrayList<Term> rhs;

    public Rule(Term lhs, ArrayList<Term> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Term getLhs() {
        return lhs;
    }

    public ArrayList<Term> getRhs() {
        return rhs;
    }
}

class Term {

}

class Symbol extends Term{
    private String value;

    public Symbol(String value) {
        this.value = value;
    }

    public boolean isTerminal() {
        return this.getClass().equals(Terminal.class);
    }


    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.getClass().equals(this.getClass())) &&
                ((Symbol)obj).getValue().equals(this.getValue());

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}

class Terminal extends Symbol {
    public Terminal(String value) {
        super(value);
    }
}

class Nonterminal extends Symbol {
    private ArrayList<Symbol> follows;
    private ArrayList<Symbol> firsts;

    public Nonterminal(String value) {
        super(value);
        follows = new ArrayList<Symbol>();
        firsts = new ArrayList<Symbol>();
    }

    public void setFollows(ArrayList<Symbol> follows) {
        this.follows = follows;
    }

    public void setFirsts(ArrayList<Symbol> firsts) {
        this.firsts = firsts;
    }

    public ArrayList<Symbol> getFollows() {
        return follows;
    }

    public ArrayList<Symbol> getFirsts() {
        return firsts;
    }
}

class ActionSymbol extends Term{
    private String functionName;

    public ActionSymbol(String functionName) {
        this.functionName = functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "\"" + functionName + "\"";
    }
}