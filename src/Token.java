public class Token {
    private String name;
    private int attr;

    public Token(String key, int attr) {
        this.name = key;
        this.attr = attr;
    }

    public Token(String key) {
        this.name = key;
    }

    public String getName() {
        return name;
    }

    public int getAttr() {
        return attr;
    }

    @Override
    public String toString() {
        return "<" + name + ", " + attr + ">";
    }
}
