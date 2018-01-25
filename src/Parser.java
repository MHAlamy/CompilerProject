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
                    keyPair = new Pair<Symbol, Symbol>((Symbol)top, inputTerminal);

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

    private void addRules() {
        rules.add(new Rule(ntGoal, new ArrayList<Term>(Arrays.asList(ntSource, tEOF)))); // 0

        rules.add(new Rule(ntSource, new ArrayList<Term>(Arrays.asList(ntClassDecs, ntMainClass)))); // 1

        rules.add(new Rule(ntMainClass, new ArrayList<Term>(Arrays.asList(tPublic, tClass, tId, tCurlyBraceOpen,
                tPublic, tStatic, tVoid, tMain, tParanOpen, tParanClose, tCurlyBraceOpen, ntVarDecs, ntStmts,
                tCurlyBraceClose, tCurlyBraceClose)))); // 2

        rules.add(new Rule(ntClassDecs, new ArrayList<Term>(Arrays.asList(ntClassDec, ntClassDecs)))); // 3
        rules.add(new Rule(ntClassDecs, new ArrayList<Term>())); // 4

        rules.add(new Rule(ntClassDec, new ArrayList<Term>(Arrays.asList(tClass, tId, ntExtension,
                tCurlyBraceOpen, ntFieldDecs, ntMethodDecs, tCurlyBraceClose)))); // 5

        rules.add(new Rule(ntExtension, new ArrayList<Term>(Arrays.asList(tExtends, tId)))); // 6
        rules.add(new Rule(ntExtension, new ArrayList<Term>())); // 7

        rules.add(new Rule(ntFieldDecs, new ArrayList<Term>(Arrays.asList(ntFieldDec, ntFieldDecs)))); // 8
        rules.add(new Rule(ntFieldDecs, new ArrayList<Term>())); // 9

        rules.add(new Rule(ntFieldDec, new ArrayList<Term>(Arrays.asList(tStatic, ntType, tId, tSemiColon)))); // 10

        rules.add(new Rule(ntVarDecs, new ArrayList<Term>(Arrays.asList(ntVarDec, ntVarDecs)))); // 11
        rules.add(new Rule(ntVarDecs, new ArrayList<Term>())); // 12

        rules.add(new Rule(ntVarDec, new ArrayList<Term>(Arrays.asList(ntType, tId, tSemiColon)))); // 13

        rules.add(new Rule(ntMethodDecs, new ArrayList<Term>(Arrays.asList(ntMethodDec, ntMethodDecs)))); // 14
        rules.add(new Rule(ntMethodDecs, new ArrayList<Term>())); // 15

        rules.add(new Rule(ntMethodDec, new ArrayList<Term>(Arrays.asList(tPublic, tStatic, ntType, tId,
                tParanOpen, ntPrmts, tParanClose, tCurlyBraceOpen, ntVarDecs, ntStmts, tReturn, ntGenExp,
                tSemiColon, tCurlyBraceClose)))); // 16

        rules.add(new Rule(ntPrmts, new ArrayList<Term>(Arrays.asList(ntType, tId, ntPrmt)))); // 17
        rules.add(new Rule(ntPrmts, new ArrayList<Term>())); // 18

        rules.add(new Rule(ntPrmt, new ArrayList<Term>(Arrays.asList(tComma, ntType, tId, ntPrmt)))); // 19
        rules.add(new Rule(ntPrmt, new ArrayList<Term>())); // 20

        rules.add(new Rule(ntType, new ArrayList<Term>(Arrays.asList(tBoolean)))); // 21
        rules.add(new Rule(ntType, new ArrayList<Term>(Arrays.asList(tInt)))); // 22

        rules.add(new Rule(ntStmts, new ArrayList<Term>(Arrays.asList(ntStmt, ntStmts)))); // 23
        rules.add(new Rule(ntStmts, new ArrayList<Term>())); // 24

        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tCurlyBraceOpen, ntStmts, tCurlyBraceClose)))); // 25
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tIf, tParanOpen, ntGenExp, tParanClose, ntStmt,
                tElse, ntStmt)))); // 26
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tWhile, tParanOpen, ntGenExp,
                tParanClose, ntStmt)))); // 27
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tFor, tParanOpen, tId, tEqual, tInteger,
                tSemiColon, ntExp, ntRelTerm, tSemiColon, tId, tPlusEqual, tInteger, tParanClose, ntStmt)))); // 28
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tId, tEqual, ntGenExp, tSemiColon)))); // 29
        rules.add(new Rule(ntStmt, new ArrayList<Term>(Arrays.asList(tSystem, tDot, tOut, tDot, tPrintln,
                tParanOpen, ntGenExp, tParanClose, tSemiColon)))); // 30

        rules.add(new Rule(ntGenExp, new ArrayList<Term>(Arrays.asList(ntExp, ntGenExp1)))); // 31

        rules.add(new Rule(ntGenExp1, new ArrayList<Term>(Arrays.asList(ntRelTerm, ntRelExp)))); // 32
        rules.add(new Rule(ntGenExp1, new ArrayList<Term>())); // 33

        rules.add(new Rule(ntRelExp, new ArrayList<Term>(Arrays.asList(tAnd, ntExp, ntRelTerm, ntRelExp)))); // 34
        rules.add(new Rule(ntRelExp, new ArrayList<Term>())); // 35

        rules.add(new Rule(ntRelTerm, new ArrayList<Term>(Arrays.asList(tDoubleEqual, ntExp)))); // 36
        rules.add(new Rule(ntRelTerm, new ArrayList<Term>(Arrays.asList(tLess, ntExp)))); // 37 ???????

        rules.add(new Rule(ntExp, new ArrayList<Term>(Arrays.asList(ntTerm, ntExp1)))); // 38

        rules.add(new Rule(ntExp1, new ArrayList<Term>(Arrays.asList(ntExp2, ntExp1)))); // 39
        rules.add(new Rule(ntExp1, new ArrayList<Term>())); // 40

        rules.add(new Rule(ntExp2, new ArrayList<Term>(Arrays.asList(tPlus, ntTerm)))); // 41
        rules.add(new Rule(ntExp2, new ArrayList<Term>(Arrays.asList(tMinus, ntTerm)))); // 42

        rules.add(new Rule(ntTerm, new ArrayList<Term>(Arrays.asList(ntFactor, ntTerm1)))); // 43

        rules.add(new Rule(ntTerm1, new ArrayList<Term>(Arrays.asList(tMult, ntFactor, ntTerm1)))); // 44
        rules.add(new Rule(ntTerm1, new ArrayList<Term>())); // 45

        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tParanOpen, ntExp, tParanClose)))); // 46
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tId, ntFactor1)))); // 47
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tTrue)))); // 48
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tFalse)))); // 49
        rules.add(new Rule(ntFactor, new ArrayList<Term>(Arrays.asList(tInteger)))); // 50

        rules.add(new Rule(ntFactor1, new ArrayList<Term>(Arrays.asList(tDot, tId, ntFactor2)))); // 51
        rules.add(new Rule(ntFactor1, new ArrayList<Term>())); // 52

        rules.add(new Rule(ntFactor2, new ArrayList<Term>(Arrays.asList(tParanOpen, ntArgs, tParanClose)))); // 53
        rules.add(new Rule(ntFactor2, new ArrayList<Term>())); // 54

        rules.add(new Rule(ntArgs, new ArrayList<Term>(Arrays.asList(ntGenExp, ntArg)))); // 55
        rules.add(new Rule(ntArgs, new ArrayList<Term>())); // 56

        rules.add(new Rule(ntArg, new ArrayList<Term>(Arrays.asList(tComma, ntGenExp, ntArg)))); // 57
        rules.add(new Rule(ntArg, new ArrayList<Term>())); // 58
    }

    private void fillParseTable() { // TODO: 1/24/18 create parse table
        parseTable.put(new Pair<Term, Term>(ntFieldDec, tStatic), rules.get(10));

        parseTable.put(new Pair<Term, Term>(ntTerm, tParanOpen), rules.get(43));
        parseTable.put(new Pair<Term, Term>(ntTerm, tFalse), rules.get(43));
        parseTable.put(new Pair<Term, Term>(ntTerm, tInteger), rules.get(43));
        parseTable.put(new Pair<Term, Term>(ntTerm, tId), rules.get(43));
        parseTable.put(new Pair<Term, Term>(ntTerm, tTrue), rules.get(43));

        parseTable.put(new Pair<Term, Term>(ntVarDec, tBoolean), rules.get(13));
        parseTable.put(new Pair<Term, Term>(ntVarDec, tInt), rules.get(13));

        parseTable.put(new Pair<Term, Term>(ntClassDecs, tClass), rules.get(3));
        parseTable.put(new Pair<Term, Term>(ntClassDecs, tPublic), rules.get(4));

        parseTable.put(new Pair<Term, Term>(ntMethodDec, tPublic), rules.get(16));

        parseTable.put(new Pair<Term, Term>(ntPrmts, tParanOpen), rules.get(18));
        parseTable.put(new Pair<Term, Term>(ntPrmts, tBoolean), rules.get(17));
        parseTable.put(new Pair<Term, Term>(ntPrmts, tInt), rules.get(17));

        parseTable.put(new Pair<Term, Term>(ntGenExp, tParanOpen), rules.get(31));
        parseTable.put(new Pair<Term, Term>(ntGenExp, tFalse), rules.get(31));
        parseTable.put(new Pair<Term, Term>(ntGenExp, tInteger), rules.get(31));
        parseTable.put(new Pair<Term, Term>(ntGenExp, tId), rules.get(31));
        parseTable.put(new Pair<Term, Term>(ntGenExp, tTrue), rules.get(31));

        parseTable.put(new Pair<Term, Term>(ntExtension, tExtends), rules.get(6));

        parseTable.put(new Pair<Term, Term>(ntPrmt, tParanOpen), rules.get(20));
        parseTable.put(new Pair<Term, Term>(ntPrmt, tComma), rules.get(19));

        parseTable.put(new Pair<Term, Term>(ntStmt, tFor), rules.get(28));
        parseTable.put(new Pair<Term, Term>(ntStmt, tId), rules.get(29));
        parseTable.put(new Pair<Term, Term>(ntStmt, tSystem), rules.get(30));
        parseTable.put(new Pair<Term, Term>(ntStmt, tIf), rules.get(26));
        parseTable.put(new Pair<Term, Term>(ntStmt, tWhile), rules.get(27));
        parseTable.put(new Pair<Term, Term>(ntStmt, tCurlyBraceOpen), rules.get(25));

        parseTable.put(new Pair<Term, Term>(ntExp, tParanOpen), rules.get(38));
        parseTable.put(new Pair<Term, Term>(ntExp, tFalse), rules.get(38));
        parseTable.put(new Pair<Term, Term>(ntExp, tInteger), rules.get(38));
        parseTable.put(new Pair<Term, Term>(ntExp, tId), rules.get(38));
        parseTable.put(new Pair<Term, Term>(ntExp, tTrue), rules.get(38));

        parseTable.put(new Pair<Term, Term>(ntTerm1, tAnd), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tParanClose), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tMult), rules.get(44));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tPlus), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tComma), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tMinus), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tSemiColon), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tLess), rules.get(45));
        parseTable.put(new Pair<Term, Term>(ntTerm1, tDoubleEqual), rules.get(45));

        parseTable.put(new Pair<Term, Term>(ntExp1, tAnd), rules.get(40));
        parseTable.put(new Pair<Term, Term>(ntExp1, tParanClose), rules.get(40));
        parseTable.put(new Pair<Term, Term>(ntExp1, tPlus), rules.get(39));
        parseTable.put(new Pair<Term, Term>(ntExp1, tComma), rules.get(40));
        parseTable.put(new Pair<Term, Term>(ntExp1, tMinus), rules.get(39));
        parseTable.put(new Pair<Term, Term>(ntExp1, tSemiColon), rules.get(40));
        parseTable.put(new Pair<Term, Term>(ntExp1, tLess), rules.get(40));
        parseTable.put(new Pair<Term, Term>(ntExp1, tDoubleEqual), rules.get(40));

        parseTable.put(new Pair<Term, Term>(ntFieldDecs, tStatic), rules.get(8));
        parseTable.put(new Pair<Term, Term>(ntFieldDecs, tPublic), rules.get(9));
        parseTable.put(new Pair<Term, Term>(ntFieldDecs, tCurlyBraceClose), rules.get(9));

        parseTable.put(new Pair<Term, Term>(ntMethodDecs, tPublic), rules.get(14));
        parseTable.put(new Pair<Term, Term>(ntMethodDecs, tCurlyBraceClose), rules.get(15));

        parseTable.put(new Pair<Term, Term>(ntGoal, tClass), rules.get(0));
        parseTable.put(new Pair<Term, Term>(ntGoal, tPublic), rules.get(0));

        parseTable.put(new Pair<Term, Term>(ntMainClass, tPublic), rules.get(2));

        parseTable.put(new Pair<Term, Term>(ntType, tBoolean), rules.get(21));
        parseTable.put(new Pair<Term, Term>(ntType, tInt), rules.get(22));

        parseTable.put(new Pair<Term, Term>(ntVarDecs, tFor), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tId), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tBoolean), rules.get(11));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tSystem), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tInt), rules.get(11));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tIf), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tWhile), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tReturn), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tCurlyBraceOpen), rules.get(12));
        parseTable.put(new Pair<Term, Term>(ntVarDecs, tCurlyBraceClose), rules.get(12));

        parseTable.put(new Pair<Term, Term>(ntClassDec, tClass), rules.get(5));

        parseTable.put(new Pair<Term, Term>(ntSource, tClass), rules.get(1));
        parseTable.put(new Pair<Term, Term>(ntSource, tPublic), rules.get(1));

        parseTable.put(new Pair<Term, Term>(ntStmts, tFor), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tId), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tSystem), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tIf), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tWhile), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tReturn), rules.get(24));
        parseTable.put(new Pair<Term, Term>(ntStmts, tCurlyBraceOpen), rules.get(23));
        parseTable.put(new Pair<Term, Term>(ntStmts, tCurlyBraceClose), rules.get(24));

        parseTable.put(new Pair<Term, Term>(ntGenExp1, tLess), rules.get(32));
        parseTable.put(new Pair<Term, Term>(ntGenExp1, tDoubleEqual), rules.get(32));

        parseTable.put(new Pair<Term, Term>(ntRelTerm, tDoubleEqual), rules.get(36));

        parseTable.put(new Pair<Term, Term>(ntRelExp, tAnd), rules.get(34));
        parseTable.put(new Pair<Term, Term>(ntRelExp, tParanClose), rules.get(35));
        parseTable.put(new Pair<Term, Term>(ntRelExp, tComma), rules.get(35));
        parseTable.put(new Pair<Term, Term>(ntRelExp, tSemiColon), rules.get(35));

        parseTable.put(new Pair<Term, Term>(ntExp2, tPlus), rules.get(41));
        parseTable.put(new Pair<Term, Term>(ntExp2, tMinus), rules.get(42));

        parseTable.put(new Pair<Term, Term>(ntArg, tParanClose), rules.get(58));
        parseTable.put(new Pair<Term, Term>(ntArg, tComma), rules.get(57));

        parseTable.put(new Pair<Term, Term>(ntFactor, tParanOpen), rules.get(46));
        parseTable.put(new Pair<Term, Term>(ntFactor, tFalse), rules.get(49));
        parseTable.put(new Pair<Term, Term>(ntFactor, tInteger), rules.get(50));
        parseTable.put(new Pair<Term, Term>(ntFactor, tId), rules.get(47));
        parseTable.put(new Pair<Term, Term>(ntFactor, tTrue), rules.get(48));

        parseTable.put(new Pair<Term, Term>(ntFactor1, tAnd), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tParanClose), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tMult), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tPlus), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tComma), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tMinus), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tDot), rules.get(51));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tSemiColon), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tLess), rules.get(52));
        parseTable.put(new Pair<Term, Term>(ntFactor1, tDoubleEqual), rules.get(52));

        parseTable.put(new Pair<Term, Term>(ntFactor2, tAnd), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tParanOpen), rules.get(53));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tParanClose), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tMult), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tPlus), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tComma), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tMinus), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tSemiColon), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tLess), rules.get(54));
        parseTable.put(new Pair<Term, Term>(ntFactor2, tDoubleEqual), rules.get(54));

        parseTable.put(new Pair<Term, Term>(ntArgs, tParanOpen), rules.get(55));
        parseTable.put(new Pair<Term, Term>(ntArgs, tParanClose), rules.get(56));
        parseTable.put(new Pair<Term, Term>(ntArgs, tFalse), rules.get(55));
        parseTable.put(new Pair<Term, Term>(ntArgs, tInteger), rules.get(55));
        parseTable.put(new Pair<Term, Term>(ntArgs, tId), rules.get(55));
        parseTable.put(new Pair<Term, Term>(ntArgs, tTrue), rules.get(55));

    }
}

class Pair <F, S> {
    // TODO: 1/25/18 compatible with hash map? ????
    F first;
    S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.getClass().equals(this.getClass())) &&
                (((Pair)obj).first.equals(this.first) && ((Pair)obj).second.equals(this.second));

    }
}