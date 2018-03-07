package hello;

public class FlatFile {
    String line;
    int number;

    public FlatFile(String line, int number) {
        this.line = line;
        this.number = number;
    }

    public String getLine() {
        return line;
    }

    public int getNumber() {
        return number;
    }
}
