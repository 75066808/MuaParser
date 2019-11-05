package UTILS;

public class StringReader {

    private int offset;
    private String string;

    public StringReader() {
        offset = 0;
        string = null;
    }

    public void reset(String string) {
        this.string = string.trim().replaceAll("\\s+", " ");
        this.offset = 0;
    }

    public void skip() {
        if (string != null) {
            while (offset < string.length() && string.charAt(offset) == ' ')  // skip blank
                offset++;
        }
    }

    public boolean isEmpty() {
        return string == null || offset >= string.length();
    }

    public void forward() {
        offset++;
    }

    public char getChar() {
        return string.charAt(offset);
    }

    public void setChar(char c) {
        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.setCharAt(offset, c);
        string = stringBuilder.toString();
    }


    public char readChar() {
        if (string == null)
            return 0;
        else
            return string.charAt(offset++);
    }

    public String readUntil(CharSequence charSequence) {
        int start = offset;
        if (string == null)
            return null;

        while (offset < string.length()) {
            for (int i = 0; i < charSequence.length(); i++)
                if (getChar() == charSequence.charAt(i))
                    return string.substring(start, offset);;
            offset++;
        }
        return string.substring(start, offset);
    }
}
