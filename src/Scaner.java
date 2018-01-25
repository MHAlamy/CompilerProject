import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Scaner {
    private ArrayList<Character> input = new ArrayList<Character>();
    private String fileAddress = "/Users/MohammadReza/Desktop/Uni/Programs/CompilerProject/src/TestFiles/input.txt";

    private ArrayList<String> keywords = new ArrayList<String>();

    private int curInputIndex = 0;
    private int curLine = 2; // for writing error message
    private Token lastToken;

    private SymbolTable curSymbolTable;

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
        curSymbolTable = st;
        readFile();

        keywords.addAll(Arrays.asList("EOF", "public", "class", "{", "static", "void", "main", "(", ")", "}",
                "extends", "return", ";", ",", "boolean", "int", "if", "else", "while", "for",
                "System", "out", "println", "true", "false", "&&", "+", "*", "-", "=", "+=", "==", ".",
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
                    } else if (nextChar == '*' || nextChar == '.' || nextChar == '(' || nextChar == ')' ||
                            nextChar == ';' || nextChar == '{' || nextChar == '}' || nextChar == '<') {
                        state = 12;
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
                    } else {
                        inpWasWrong = true;
                        state = 0; // ERROR
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
                        String before = lastToken.getName();
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
                        String before = lastToken.getName();
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
                    } else { // ignore last &
                        curRead = "";
                        state = 0;
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
                    // TODO: 1/24/18 error message
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
        } else if (keywords.contains(curRead)) {
            nextToken = new Token(curRead, -1);
        } else if (isStringNumber(curRead)) {
            nextToken = new Token("integer", Integer.parseInt(curRead));
        } else { // it's an identifier
//            nextToken = new Token("identifier", 0);
//            nextToken = new Token(curRead, 0);
            nextToken = new Token("id", 0);


            /*SymbolTable tmpST = curSymbolTable;
            boolean wasFound = false;
            while (tmpST != null) {
                if (tmpST.getIdIndex(curRead) != -1) { // was found
                    wasFound = true;
                    break;
                } else {
                    tmpST = tmpST.getContainer();
                }
            }
            if (wasFound) {
                // ???
            } else { // add to curSymbolTable
//                int index = curSymbolTable.insertId(curRead);
                // index???
            }*/
            // TODO: 1/22/18 how to return this ST's info?
            // TODO: 1/21/18 fix index in symbol table
        }


        lastToken = nextToken;

        return nextToken;
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
