package UTILS;

public class LineReader {

    private int offset;
    private String []strings;

    public LineReader(String []strings) {
        this.offset = 0;
        this.strings = strings;
    }

    public String readString() {
        return strings[offset++];
    }


}
