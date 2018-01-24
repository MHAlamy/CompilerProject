import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Parser {
    private Stack<Term> parseStack;
    private Token curToken;
    private ArrayList<Rule> rules;

    private HashMap<Pair<Term, Term>, Rule> parseTable;

    private Scaner scaner; // ???

    private Terminal tEOF, tPublic, tClass, tId;
    private Nonterminal ntGoal, ntSource, ntClassDecs;


    public Parser() {
        rules = new ArrayList<Rule>();
        parseTable = new HashMap<Pair<Term, Term>, Rule>();

        completeTerms();
        addRules();
        fillParseTable();

        scaner = new Scaner(null);

        parseStack.push(new Terminal("$"));
        parseStack.push(new Nonterminal("Goal"));
    }

    public void startParsing() {
        Token nextToken = scaner.getNextToken();
        String tokenName;
        Terminal inputTerminal;
        Pair keyPair;
        Rule targetRule;

        while (!parseStack.peek().equals(new Terminal("$")) &&
                nextToken != null) {
            Term top = parseStack.peek();
            tokenName = nextToken.getName();

            if (top.getClass().equals(Symbol.class)) {
                if (((Symbol)top).isTerminal()) { // check if two symbols are the same
                    if (tokenName.equals(((Symbol)top).getValue())) { // remove both
                        parseStack.pop();
                        nextToken = scaner.getNextToken();
                    } else {
                        parseStack.pop();
                        int tmpCII = scaner.getCurInputIndex() - nextToken.getName().length();
                        System.out.println("Parsing Error at line " + scaner.getCurLine() + " and character " +
                        tmpCII + ". Input was " + nextToken + ", but expected (and read) " + top + ".");
                    }
                } else { // use a rule
                    inputTerminal = new Terminal(nextToken.getName());
                    keyPair = new Pair<Term, Term>(top, inputTerminal);

                    if (parseTable.containsKey(keyPair)) { // use production rule
                        targetRule = parseTable.get(keyPair);

                        parseStack.pop();

                        for (int i = targetRule.getRhs().size() - 1; i >= 0; i--) {
                            parseStack.push(targetRule.getRhs().get(i)); // insert RHS terms from right to left
                        }

                    } else {
                        int oldTmpCII = scaner.getCurInputIndex() - nextToken.getName().length();
                        while (nextToken != null &&  // while nextToken is not in follow set of top
                                !((Nonterminal)top).getFollows().contains(new Symbol(nextToken.getName()))) {
                            nextToken = scaner.getNextToken();
                        }
                        if (nextToken != null) {
                            parseStack.pop();
                            int newTmpCII = scaner.getCurInputIndex() - nextToken.getName().length();
                            System.out.println("Parsing Error at line " + scaner.getCurLine() + " and character " +
                                    oldTmpCII + ". Couldn't match input with non-terminal " + top +
                                    " .Dumped characters " + oldTmpCII + " until " + newTmpCII + " + non-terminal");
                        } else { // what should do ???
                            System.out.println("ERROR! COULDN'T FIX IT!");
                        }
                    }
                }


            } else { // top is ActionSymbol
                // TODO: 1/25/18 Intermediate code and symbol table
            }

        }
    }

    private void completeTerms() { // TODO: 1/25/18 Add all terms with follows

    }
    private void addRules() { // TODO: 1/24/18 should add all rules in here

    }

    private void fillParseTable() { // TODO: 1/24/18 create parse table

    }
}

class Pair <F, S> {
    // TODO: 1/25/18 compatible with hash map?
    F first;
    S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}