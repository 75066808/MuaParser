package UTILS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringReader {

    private int offset;
    private String string;

    public StringReader() {
        this.offset = 0;
        this.string = null;
    }

    public StringReader(String string) {
        this.offset = 0;
        this.string = string.trim().replaceAll("\\s+", " ");
    }

    public void reset(String string) {
        this.string = string.trim().replaceAll("\\s+", " ");
        this.offset = 0;
    }

    public boolean empty() {
        return string == null || offset >= string.length();
    }


    public void skip() {
        if (string != null) {
            while (offset < string.length() && string.charAt(offset) == ' ')  // skip blank
                offset++;
        }
    }
    public void stop() {
        offset = string.length();
    }
    public void forward() { offset++; }

    public char getChar() {
        return string.charAt(offset);
    }

    public char readChar() { // read one char and go forword
        if (string == null)
            return 0;
        else
            return string.charAt(offset++);
    }
    public String readString(String regex) { // read the string match the regex
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string.substring(offset));

        if (!matcher.find() || matcher.start() != 0)
            return null;

        offset += matcher.end();
        return matcher.group();
    }
}
