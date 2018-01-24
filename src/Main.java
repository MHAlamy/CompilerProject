public class Main {
    public static void main(String[] args) {
        Scaner scaner = new Scaner(null); // ???

        Token tmp;

        int i = 0;
        do {
            i++;
            tmp = scaner.getNextToken();
            System.out.println(tmp + " ");
        } while (!tmp.getName().equals("EOF"));
//        } while (i != 100);
    }

}