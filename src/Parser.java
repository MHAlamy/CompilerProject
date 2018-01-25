import java.util.ArrayList;
import java.util.Arrays;
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
                } else { // use a rule // TODO: 1/25/18 handle epsilon RHSs
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

    private void completeTerms() { // TODO: 1/25/18 Add follows
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
        rules.add(new Rule(ntGoal, new ArrayList<Term>(Arrays.asList(ntSource, tEOF))));
        rules.add(new Rule(ntSource, new ArrayList<Term>(Arrays.asList(ntClassDecs, ntMainClass))));
        rules.add(new Rule(ntMainClass, new ArrayList<Term>(Arrays.asList(tPublic, tClass, tId, tCurlyBraceOpen,
                tPublic, tStatic, tVoid, tMain, tParanOpen, tParanClose, tCurlyBraceOpen, ntVarDecs, ntStmts,
                tCurlyBraceClose, tCurlyBraceClose))));
        rules.add(new Rule(ntClassDecs, new ArrayList<Term>(Arrays.asList(ntClassDec, ntClassDecs))));
        rules.add(new Rule(ntClassDecs, new ArrayList<Term>()));
        rules.add(new Rule(ntClassDec, new ArrayList<Term>(Arrays.asList(tClass, tId, ntExtension,
                tCurlyBraceOpen, ntFieldDecs, ntMethodDecs, tCurlyBraceClose))));
        rules.add(new Rule(ntExtension, new ArrayList<Term>(Arrays.asList(tExtends, tId))));
        rules.add(new Rule(ntExtension, new ArrayList<Term>()));
        rules.add(new Rule(ntFieldDecs, new ArrayList<Term>(Arrays.asList(ntFieldDec, ntFieldDecs))));
        rules.add(new Rule(ntFieldDecs, new ArrayList<Term>()));

        rules.add(new Rule(ntFieldDec, new ArrayList<Term>(Arrays.asList(tStatic, ntType, tId, tSemiColon))));

        rules.add(new Rule(ntVarDecs, new ArrayList<Term>(Arrays.asList(ntVarDec, ntVarDecs))));
        rules.add(new Rule(ntVarDecs, new ArrayList<Term>()));

        rules.add(new Rule(ntVarDec, new ArrayList<Term>(Arrays.asList(ntType, tId, tSemiColon))));

        rules.add(new Rule(ntMethodDecs, new ArrayList<Term>(Arrays.asList(ntMethodDec, ntMethodDecs))));
        rules.add(new Rule(ntMethodDecs, new ArrayList<Term>()));

        rules.add(new Rule(ntMethodDec, new ArrayList<Term>(Arrays.asList(tPublic, tStatic, ntType, tId,
                tParanOpen, ntPrmts, tParanClose, tCurlyBraceOpen, ntVarDecs, ntStmts, tReturn, ntGenExp,
                tSemiColon, tCurlyBraceClose))));

        rules.add(new Rule(ntPrmts, new ArrayList<Term>(Arrays.asList(ntType, tId, ntPrmt))));
        rules.add(new Rule(ntPrmts, new ArrayList<Term>()));

        rules.add(new Rule(ntPrmt, new ArrayList<Term>(Arrays.asList(tComma, ntType, tId, ntPrmt))));
        rules.add(new Rule(ntPrmt, new ArrayList<Term>()));

        rules.add(new Rule(ntType, new ArrayList<Term>(Arrays.asList(tBoolean))));
        rules.add(new Rule(ntType, new ArrayList<Term>(Arrays.asList(tInt))));

        rules.add(new Rule(ntStmts, new ArrayList<Term>(Arrays.asList(ntStmt, ntStmts))));
        rules.add(new Rule(ntStmts, new ArrayList<Term>()));

        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tCurlyBraceOpen, ntStmts, tCurlyBraceClose))));
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tIf, tParanOpen, ntGenExp, tParanClose, ntStmt,
                tElse, ntStmt))));
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tWhile, tParanOpen, ntGenExp,
                tParanClose, ntStmt))));
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tFor, tParanOpen, tId, tEqual, tInteger,
                tSemiColon, ntExp, ntRelTerm, tSemiColon, tId, tPlusEqual, tInteger, tParanClose, ntStmt))));
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tId, tEqual, ntGenExp, tSemiColon))));
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tSystem, tDot, tOut, tDot, tPrintln,
                tParanOpen, ntGenExp, tParanClose, tSemiColon))));

        rules.add(new Rule(ntGenExp, new ArrayList<Term>(Arrays.asList(ntExp, ntGenExp1))));

        rules.add(new Rule(ntGenExp1, new ArrayList<Term>(Arrays.asList(ntRelTerm, ntRelExp))));
        rules.add(new Rule(ntGenExp1, new ArrayList<Term>()));

        rules.add(new Rule(ntRelExp, new ArrayList<Term>(Arrays.asList(tAnd, ntExp, ntRelTerm, ntRelExp))));
        rules.add(new Rule(ntRelExp, new ArrayList<Term>()));

        rules.add(new Rule(ntRelTerm, new ArrayList<Term>(Arrays.asList(tDoubleEqual, ntExp))));
        rules.add(new Rule(ntRelTerm, new ArrayList<Term>(Arrays.asList(tLess, ntExp))));

        rules.add(new Rule(ntExp, new ArrayList<Term>(Arrays.asList(ntTerm, ntExp1))));

        rules.add(new Rule(ntExp1, new ArrayList<Term>(Arrays.asList(ntExp2, ntExp1))));
        rules.add(new Rule(ntExp1, new ArrayList<Term>()));

        rules.add(new Rule(ntExp2, new ArrayList<Term>(Arrays.asList(tPlus, ntTerm))));
        rules.add(new Rule(ntExp2, new ArrayList<Term>(Arrays.asList(tMinus, ntTerm))));

        rules.add(new Rule(ntTerm, new ArrayList<Term>(Arrays.asList(ntFactor, ntTerm1))));

        rules.add(new Rule(ntTerm1, new ArrayList<Term>(Arrays.asList(tMult, ntFactor, ntTerm1))));
        rules.add(new Rule(ntTerm1, new ArrayList<Term>()));

        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tParanOpen, ntExp, tParanClose))));
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tId, ntFactor1))));
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tTrue))));
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tFalse))));
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tInteger))));

        rules.add(new Rule(ntFactor1, new ArrayList<Term>(Arrays.asList(tDot, tId, ntFactor2))));
        rules.add(new Rule(ntFactor1, new ArrayList<Term>()));

        rules.add(new Rule(ntFactor2, new ArrayList<Term>(Arrays.asList(tParanOpen, ntArgs, tParanClose))));
        rules.add(new Rule(ntFactor2, new ArrayList<Term>()));

        rules.add(new Rule(ntArgs, new ArrayList<Term>(Arrays.asList(ntGenExp, ntArg))));
        rules.add(new Rule(ntArgs, new ArrayList<Term>()));

        rules.add(new Rule(ntArg, new ArrayList<Term>(Arrays.asList(tComma, ntGenExp, ntArg))));
        rules.add(new Rule(ntArg, new ArrayList<Term>()));

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