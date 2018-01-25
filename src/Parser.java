import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Parser {
    private Stack<Term> parseStack;
    private Token curToken;
    private ArrayList<Rule> rules;

    private HashMap<Pair<Term, Term>, Rule> parseTable;

    private Scaner scaner; // ???

    private Terminal tEOF, tPublic, tClass, tId, tStatic, tVoid, tMain, tExtends, tReturn, tBoolean, tInt,
        tIf, tElse, tWhile, tFor, tInteger, tTrue, tFalse, tSystem, tOut, tPrintln,
        tParanOpen, tParanClose, tCurlyBraceOpen, tCurlyBraceClose, tSemiColon, tComma, tDot, tMinus, tPlus, tMult,
            tEqual, tPlusEqual, tDoubleEqual, tAnd, tLess;

    private Nonterminal ntGoal, ntSource, ntClassDecs, ntClassDec, ntMainClass, ntExtension, ntVarDecs, ntStmts, ntStmt,
        ntFieldDecs, ntFieldDec, ntMethodDecs, ntType, ntVarDec, ntMethodDec, ntPrmts, ntPrmt, ntGenExp, ntGenExp1,
    ntExp, ntExp1, ntExp2, ntRelExp, ntRelTerm, ntTerm, ntTerm1, ntFactor, ntFactor1, ntFactor2, ntArg, ntArgs;


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
        tEOF = new Terminal("EOF");
        tPublic = new Terminal("public");
        tClass = new Terminal("class");
        tId = new Terminal("id");
        tStatic = new Terminal("static");
        tVoid = new Terminal("void");
        tMain = new Terminal("main");
        tExtends = new Terminal("extends");
        tReturn = new Terminal("return");
        tBoolean = new Terminal("boolean");
        tInt = new Terminal("int");
        tIf = new Terminal("if");
        tElse = new Terminal("else");
        tWhile = new Terminal("while");
        tFor = new Terminal("for");
        tInteger = new Terminal("integer");
        tTrue = new Terminal("true");
        tFalse = new Terminal("false");
        tSystem = new Terminal("System");
        tOut = new Terminal("out");
        tPrintln = new Terminal("println");
        tParanOpen = new Terminal("(");
        tParanClose = new Terminal(")");
        tCurlyBraceOpen = new Terminal("{");
        tCurlyBraceClose = new Terminal("}");
        tSemiColon = new Terminal(";");
        tComma = new Terminal(",");
        tDot = new Terminal(".");
        tMinus = new Terminal("-");
        tPlus = new Terminal("+");
        tMult = new Terminal("*");
        tEqual = new Terminal("=");
        tPlusEqual = new Terminal("+=");
        tDoubleEqual = new Terminal("==");
        tAnd = new Terminal("&&");
        tLess = new Terminal("<");

        ntGoal = new Nonterminal("Goal");
        ntSource = new Nonterminal("Source");
        ntClassDecs = new Nonterminal("ClassDecs");
        ntClassDec = new Nonterminal("ClassDec");
        ntMainClass = new Nonterminal("MainClass");
        ntExtension = new Nonterminal("Extension");
        ntVarDecs = new Nonterminal("VarDecs");
        ntStmts = new Nonterminal("Stmts");
        ntStmt = new Nonterminal("Stmt");
        ntFieldDecs = new Nonterminal("FieldDecs");
        ntFieldDec = new Nonterminal("FieldDec");
        ntMethodDecs = new Nonterminal("MethodDecs");
        ntType = new Nonterminal("Type");
        ntVarDec = new Nonterminal("VarDec");
        ntMethodDec = new Nonterminal("MethodDec");
        ntPrmts = new Nonterminal("Prmts");
        ntPrmt = new Nonterminal("Prmt");
        ntGenExp = new Nonterminal("GenExp");
        ntGenExp1 = new Nonterminal("GenExp1");
        ntExp = new Nonterminal("Exp");
        ntExp1 = new Nonterminal("Exp1");
        ntExp2 = new Nonterminal("Exp2");
        ntRelExp = new Nonterminal("RelExp");
        ntRelTerm = new Nonterminal("RelTerm");
        ntTerm = new Nonterminal("Term");
        ntTerm1 = new Nonterminal("Term");
        ntFactor = new Nonterminal("Factor");
        ntFactor1 = new Nonterminal("Factor1");
        ntFactor2 = new Nonterminal("Factor2");
        ntArg = new Nonterminal("Arg");
        ntArgs = new Nonterminal("Args");
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