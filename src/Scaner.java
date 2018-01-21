import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Scaner {
    private ArrayList<Character> input = new ArrayList<Character>();
    private String fileAddress = "/Users/MohammadReza/Desktop/Uni/Programs/CompilerProject/src/input.txt";

    private ArrayList<String> keywords = new ArrayList<String>();

    private int curInputIndex = 0;

    public Scaner() {
        readFile();

        keywords.addAll(Arrays.asList("EOF", "public", "class", "{", "static", "void", "main", "(", ")", "}",
                "extends", "return", ";", ",", "boolean", "int", "if", "else", "whiel", "for",
                "System.out.println", "true", "false", "&&", "+", "*", "-", "=", "+=", "==",
                "identifier", "integer"));
//        for (char c:
//            input){
//            System.out.print(c);
//        }
    }

    public Token getNextToken() {
        Token nextToken = new Token(""); // REMOVE

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
                    if (nextChar == '-') {
                        state = 1;
                    } else if (nextChar == '+') {
                        state = 2;
                    } else if (nextChar == '*' || nextChar == '.' || nextChar == '(' || nextChar == ')' ||
                            nextChar == ';' || nextChar == '{' || nextChar == '}' || nextChar == '<') {
                        state = 12;
                    } else if (nextChar == '=') {
                        state = 5;
                    } else if (Character.isDigit(nextChar)) {
                        state = 6;
                    } else if (Character.isLetter(nextChar)) {
                        state = 7;
                    } else if (nextChar == '/') {
                        state = 8;
                    } else if (Character.isWhitespace(nextChar)) {
                        state = 0;
                    } else {
                        state = -1; // ERROR
                    }

                    if (!Character.isWhitespace(nextChar)) {
                        curRead += nextChar;
                    }
                    curInputIndex++;

                    break;

                case 1:  // has read a -
                    if (Character.isDigit(nextChar)) {
                        // TODO: 1/21/18
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
                        // TODO: 1/21/18
                    } else { // token is only +, don't read the rest
                        state = 12;
                    }
                    break;


                case 3:  // after first -, a digit

                    break;

                case 4: // after first +, a digit

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
                    } else {
                        state = 8;
                        //ERROR
                    }
                    curInputIndex++;

                    break;

                case 9:  // second /
                    if (nextChar == '\n') { // comment is finished
                        curRead = "";
                        state = 0;
                    } else { //
                        state = 9;
                    }
                    curInputIndex++;

                    break;

                case 10:  // finished /*
                    if (nextChar == '*') {
                        state = 11;
                    } else {
                        state = 10;
                    }
                    curInputIndex++;
                    break;

                case 11: // read a * in comment
                    if (nextChar == '/') { // finish comment, start over ???
                        curRead = "";
                        state = 0;
                    } else {
                        state = 10; // continue comment
                    }
                    curInputIndex++;

                    break;

                case 12:
                    break loop;

                default: // error has occurred
                    curRead = "";
                    state = 0;
                    // TODO: 1/21/18 error handling?

            }
//            System.out.println("state = " + state + " cur pos = " + curInputIndex + " next char = " + nextChar +
//                    " cur token " + nextToken);

        }

        // check symbol table
        if (keywords.contains(curRead)) {
            nextToken = new Token(curRead, -1);
        } else if (isStringNumber(curRead)) {
            nextToken = new Token("integer", Integer.parseInt(curRead));
        } else { // it's an identifier
//            nextToken = new Token("identifier", 0);
            nextToken = new Token(curRead, 0);
            // TODO: 1/21/18 fix index in symbol table
        }

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
        if (arr[0] != '-' && arr[0] != '+' && !Character.isDigit(arr[0]))
            return false;

        for (int i = 1; i < arr.length; i++) {
            if (!Character.isDigit(arr[i]))
                return false;
        }
        return true;
    }
}
