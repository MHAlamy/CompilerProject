public class Token {
    private String key;
    private int val;

    public Token(String key, int val) {
        this.key = key;
        this.val = val;
    }

    public Token(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
