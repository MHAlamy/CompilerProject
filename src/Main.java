public class Main {
    public static void main(String[] args) {
        Scaner scaner = new Scaner();

        Token tmp;

        do {
            tmp = scaner.getNextToken();
            System.out.println(tmp + " ");
            try {
//                Thread.sleep(50);
            } catch (Exception e) {
                System.out.printf("hi");
            }
        } while (!tmp.getName().equals("EOF"));
    }
}