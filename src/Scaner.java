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

        keywords.addAll(Arrays.asList("if", "then"));
        for (char c:
            input){
            System.out.print(c);
        }
    }

    public Token getNextToken() {
        Token nextToken = new Token(""); // REMOVE

        int state = 0;
        String curRead = "";

        char nextChar = ' ';

        while (true) {
            if (curInputIndex > input.size()) {
                state = 12;
            } else {
                nextChar = input.get(curInputIndex);
            }

            switch (state) {
                case 0: {

                }
                case 1: { // has read a -

                }
                case 2: { // has read a +

                }
                case 3: { // after first -, a digit

                }
                case 4: { // after first +, a digit

                }
                case 5: { // has read a =

                }
                case 6: { // has read a digit

                }
                case 7: { // has read a letter

                }
                case 8: { // has read a /

                }
                case 9: { // second /

                }
                case 10: { // finished /*

                }
                case 11: { // read a * in comment
                    if (nextChar == '/') { // finish comment, start over ???
                        state = 0;
                        curRead = "";
                        curInputIndex++;
                    } else {
                        state = 10; // continue comment
                        curInputIndex++;
                    }
                }
                case 12: {
                    nextToken = new Token(curRead, 0); // TODO: 1/21/18
                    break;
                }
                default: { // error has occurred

                }


            }

            break;
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
            System.out.println(e);
        }
    }
}
