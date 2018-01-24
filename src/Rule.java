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
}

class Terminal extends Symbol {
    public Terminal(String value) {
        super(value);
    }
}

class Nonterminal extends Symbol {
    ArrayList<Symbol> follows;

    public Nonterminal(String value) {
        super(value);
        follows = new ArrayList<Symbol>();
    }

    public void setFollows(ArrayList<Symbol> follows) {
        this.follows = follows;
    }

    public ArrayList<Symbol> getFollows() {
        return follows;
    }
}

class ActionSymbol extends Term{

}