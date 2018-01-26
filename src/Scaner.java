import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Scaner {
    private ArrayList<Character> input = new ArrayList<Character>();
    private String fileAddress = "/Users/MohammadReza/Desktop/Uni/Programs/CompilerProject/src/TestFiles/SybolTableTest.txt";

    private ArrayList<String> keywords = new ArrayList<String>();

    private int curInputIndex = 0;
    private int curLine = 1; // for writing error message
    private Token lastToken;

    private String stState; // classDef, extendsThis, normalDef, useId,

    private SymbolTable masterSymbolTable;
    private SymbolTable curSymbolTable;

    public void setSTState(String STState) {
        this.stState = STState;
    }

    public void setCurSymbolTable(SymbolTable curSymbolTable) {
        this.curSymbolTable = curSymbolTable;
    }

    public int getCurInputIndex() {
        return curInputIndex;
    }

    public int getCurLine() {
        return curLine;
    }

    public Scaner(SymbolTable st) {
        this.masterSymbolTable = st;
        masterSymbolTable.setName("Master Table");
        curSymbolTable = st;

        stState = "";
        readFile();

        keywords.addAll(Arrays.asList("EOF", "public", "class", "{", "static", "void", "main", "(", ")", "}",
                "extends", "return", ";", ",", "boolean", "int", "if", "else", "while", "for",
                "System", "out", "println", "true", "false", "&&", "+", "*", "-", "=", "+=", "==", ".", "<",
                "identifier", "integer"));
//        }
    }

    public Token getNextToken() {
        Token nextToken; // REMOVE

        int state = 0;
        String curRead = "";

        char nextChar = ' ';

        loop: while (true) {
            if (curInputIndex >= input.size()) {
                state = 12;
            } else {
                nextChar = input.get(curInputIndex);
            }

            switch (state) {
                case 0:
                    boolean inpWasWrong = false;

                    if (nextChar == '-') {
                        state = 1;
                    } else if (nextChar == '+') {
                        state = 2;
                    } else if (nextChar == '*' || nextChar == '(' || nextChar == ')' || nextChar == ';' ||
                            nextChar == '{' || nextChar == '}' || nextChar == '<' || nextChar == ',') {
                        state = 12;
                    } else if (nextChar == '.') {
                        state = 51;
                    } else if (nextChar == '=') {
                        state = 5;
                    } else if (nextChar == '&') {
                        state = 52;
                    } else if (Character.isDigit(nextChar)) {
                        state = 6;
                    } else if (Character.isLetter(nextChar)) {
                        state = 7;
                    } else if (nextChar == '/') {
                        state = 8;
                    } else if (Character.isWhitespace(nextChar)) {
                        inpWasWrong = true;
                        state = 0;
                        if (lastToken != null && lastToken.getName().equals(".")) {
                            System.out.println("Scanner error at line " + curLine + " and character " + curInputIndex + ": " +
                                    "Your'e not allowed to type whitespaces after '.' token. Read whitespace will be ignored");
                        }
                    } else {
                        inpWasWrong = true;
                        state = 0; // ERROR
                        System.out.println("Scanner error at line " + curLine + " and character " + curInputIndex + ": " +
                                "Read illegal character '" + nextChar + "'. It will be ignored");
                    }

                    if (!inpWasWrong) {
                        curRead += nextChar;
                    }

                    curInputIndex++;
                    if (nextChar == '\n')
                        curLine++;

                    break;

                case 1:  // has read a -
                    if (Character.isDigit(nextChar)) {
                        String before = (lastToken == null) ? "" : lastToken.getName();

                        if (before.equals("=") || before.equals("==") || before.equals("-") || before.equals("+") ||
                                before.equals("*") || before.equals("<") || before.equals("(") || before.equals(",") ||
                                before.equals("+=")) {
                            // operator is unary, take the rest
                            curRead += nextChar;
                            curInputIndex++;
                            state = 3;
                        } else {
                            // operator is binary, just use -
                            state = 12;
                        }
                    } else { // token is only -, don't read the rest
                        state = 12;
                    }
                    break;

                case 2: // has read a +
                    if (nextChar == '=') { // += is the token
                        curRead += nextChar;
                        curInputIndex++;
                        state = 12;
                    } else if (Character.isDigit(nextChar)) {
                        String before = (lastToken == null) ? "" : lastToken.getName();
                        if (before.equals("=") || before.equals("==") || before.equals("-") || before.equals("+") ||
                                before.equals("*") || before.equals("<") || before.equals("(") || before.equals(",") ||
                                before.equals("+=")) {
                            // operator is unary, take the rest
                            curRead += nextChar;
                            curInputIndex++;
                            state = 4;
                        } else {
                            // operator is binary, just use -
                            state = 12;
                        }
                    } else { // token is only +, don't read the rest
                        state = 12;
                    }
                    break;


                case 3:  // after first -, an integer
                    if (Character.isDigit(nextChar)) {
                        curRead += nextChar;
                        curInputIndex++;
                        state = 3;
                    } else {
                        state = 12;
                    }
                    break;

                case 4: // after first +, a digit
                    if (Character.isDigit(nextChar)) {
                        curRead += nextChar;
                        curInputIndex++;
                        state = 4;
                    } else {
                        state = 12;
                    }
                    break;

                case 51: // has read a '.'. should just check last char for spaces and printing error message.
                    if (curInputIndex > 0 && Character.isWhitespace(input.get(curInputIndex))) {
                        System.out.println("Scanner error at line " + curLine + " and character " + curInputIndex + ": " +
                                "Your'e not allowed to type whitespaces before '.' token. Read whitespaces were ignored");
                    }
                    state = 12;
                    break;
                case 5: // has read a =
                    if (nextChar == '=') { // read ==, == is the token
                        curRead += nextChar;
                        curInputIndex++;
                        state = 12;
                    } else { // only = is a token
                        state = 12;
                    }
                    break;

                case 52: // has read &
                    if (nextChar == '&') {
                        curRead += nextChar;
                        state = 12;
                        curInputIndex++;
                    } else { // ignore last &
                        curRead = "";
                        state = 0;
                        System.out.println("Scanner error at line " + curLine + " and character " + curInputIndex + ": " +
                                "Expected &, but received '" + nextChar + "'. Ignoring read &.");
                    }
//                    curInputIndex++;
                    break;
                case 6: // has read a digit
                    if (Character.isDigit(nextChar)) { // continue number
                        curRead += nextChar;
                        curInputIndex++;
                        state = 6;
                    } else { // number has finished
                        state = 12;
                    }
                    break;

                case 7: // has read a letter
                    if (Character.isLetter(nextChar) || Character.isDigit(nextChar)) { // continue number
                        curRead += nextChar;
                        curInputIndex++;
                        state = 7;
                    } else { // letters are finished
                        state = 12;
//                        curInputIndex++;
                    }
                    break;

                case 8:  // has read a /
                    if (nextChar == '*') { // multiline comment
                        state = 10;
                    } else if (nextChar == '/') { // one line comment
                        state = 9;
                    } else { // ignore last /
                        curRead = "";
                        state = 0;
                        //ERROR
                        System.out.println("Scanner error at line " + curLine + " and character " + curInputIndex + ": " +
                                "Expected * or / after /, but received '" + nextChar + "'. Ignoring read /.");
                    }
                    curInputIndex++;
                    if (nextChar == '\n')
                        curLine++;

                    break;

                case 9:  // second /
                    if (nextChar == '\n') { // comment is finished
                        curRead = "";
                        state = 0;
                    } else { //
                        state = 9;
                    }
                    curInputIndex++;
                    if (nextChar == '\n')
                        curLine++;

                    break;

                case 10:  // finished /*
                    if (nextChar == '*') {
                        state = 11;
                    } else {
                        state = 10;
                    }
                    curInputIndex++;
                    if (nextChar == '\n')
                        curLine++;
                    break;

                case 11: // read a * in comment
                    if (nextChar == '/') { // finish comment, start over ???
                        curRead = "";
                        state = 0;
                    } else if (nextChar == '*') {
                        state = 11;
                    } else {
                        state = 10; // continue comment
                    }
                    curInputIndex++;
                    if (nextChar == '\n')
                        curLine++;

                    break;

                case 12:
                    break loop;

                default: // error has occurred
                    curRead = "";
                    state = 0;
                    System.out.println("SCANNER ERROR");

            }
//            System.out.println("state = " + state + " cur pos = " + curInputIndex + " next char = " + nextChar +
//                    " cur token " + nextToken);

        }

        // check symbol table
        if (curRead.equals("")) { // returns null if it reaches the end of input
            nextToken = null;
        } else if (isStringNumber(curRead)) {
            nextToken = new Token("integer", new Index(Integer.parseInt(curRead)));
        } else if (keywords.contains(curRead)) {
            nextToken = new Token(curRead, null);
        } else { // it's an identifier
//            nextToken = new Token("identifier", 0);
//            nextToken = new Token(curRead, 0);
//            nextToken = new Token("id", new Index());
            nextToken = fixIdToken(curRead);

            // TODO: 1/22/18 how to return this ST's info?
            // TODO: 1/21/18 fix index in symbol table
        }


        lastToken = nextToken;

        return nextToken;
    }

    private Token fixIdToken(String curRead) {
//        System.out.println("Adding ID " + curRead + ". state is : " + stState + "\n");

        SymbolTable tmpST = curSymbolTable;
        Index index = null;
        Token res;

        if (stState.equals("classDef")) {
            Index foundIndex = masterSymbolTable.getRow(curRead);

            if (foundIndex == null) { // ok, add new class
                foundIndex = masterSymbolTable.insertRow(curRead);
                Row tmpSR = foundIndex.getRowPointer();

                tmpSR.setType("class");
                tmpSR.setTarget(new SymbolTable(null)); // container will be set if there is extends afterward
//                tmpSR.getTarget().set;
                tmpSR.getTarget().setClass(true);
                tmpSR.getTarget().setName(curRead);
                setCurSymbolTable(tmpSR.getTarget());

                res = new Token("id", foundIndex);
                // TODO: 1/26/18 change table???
            } else { // error, return found class??
                System.out.println("Error at line " + curLine + ". Class " + curRead +
                        " was already defined. this input will be counted as old class'");
                res = new Token("id", foundIndex);
            }
        } else if (stState.equals("extendsThis")) {
            Index foundIndex = masterSymbolTable.getRow(curRead);

            if (foundIndex == null) { // error
                System.out.println("Extended class " + curRead + " does not exist!");
                res = null;
//                return new Token("id", foundIndex);
            } else { // return old defined class, set container of last one
                curSymbolTable.setContainer(foundIndex.getRowPointer().getTarget());

                res = new Token("id", foundIndex);
            }

        } else {
            boolean wasFound = false;

            while (tmpST != null) {
                index = tmpST.getRow(curRead);

                if (index != null) { // was found
                    wasFound = true;
                    break;
                } else {
                    tmpST = tmpST.getContainer();
                }
            }

            if (wasFound) {
                if (stState.equals("normalDef")) {
                    if (tmpST.equals(curSymbolTable)) {
                        System.out.println("Variable/Function " + curRead + " is already in this scope." +
                                " this declaration will be ignored");
                        // TODO: 1/26/18 what to do if ID is for function?
                        res = new Token("id", index);
                    } else { // declare in current symbol table
                        Index insertIndex;
                        insertIndex = curSymbolTable.insertRow(curRead);

                        // details about symbolRow can be set later?

//                        Row tmpSR = foundIndex.getRowPointer();

//                        tmpSR.setType("class");
//                        tmpSR.setTarget(new SymbolTable(null)); // container will be set if there is extends afterward
//                        tmpSR.getTarget().setClass(true);
//                        setCurSymbolTable(tmpSR.getTarget());

                        res =  new Token("id", insertIndex);
                    }
                } else {
                    res = new Token("id", index);
//                    System.out.println("ACCESSED " + curRead + ", inside " + index.getRowPointer().getContainer().getName() + "\n");
                }
                // if state is useID or variable was already defined:


            } else { // add to curSymbolTable
                if (stState.equals("normalDef")) {
                    Index insertIndex;
                    insertIndex = curSymbolTable.insertRow(curRead);

                    // details about symbolRow can be set later?

                    res = new Token("id", insertIndex);
                } else { // useID
                    System.out.println("Variable/Function " + curRead + " is not defined in this scope");
                    // TODO: 1/26/18 error handling??
                    res = new Token("id", null);
                }

//                index = curSymbolTable.insertRow(curRead);
//                return new Token("id", index);
            }
        }

        stState = "";

//        System.out.println("Symbol Table Result: ");
//        System.out.println(masterSymbolTable + "\n\n");
        return res;

    }

    private void readFile() { // reads the file char by char and puts it in input ArrayList
        try {
            File file = new File(fileAddress);
            FileInputStream fileInputStream = new FileInputStream(file);

            while (fileInputStream.available() > 0) {
                input.add( (char)fileInputStream.read() );
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isStringNumber(String read) {
        char[] arr = read.toCharArray();

        if (arr.length == 0 || (arr[0] != '-' && arr[0] != '+' && !Character.isDigit(arr[0])))
            return false;

        for (int i = 1; i < arr.length; i++) {
            if (!Character.isDigit(arr[i]))
                return false;
        }
        return true;
    }
}
