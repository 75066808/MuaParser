package INTERPRETER;

import UTILS.GeneralType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineReader {

    private int offset;
    private String string;

    public LineReader(String string) {
        this.offset = 0;
        this.string = string.trim().replaceAll("\\s+"," ");
    }

    public GeneralType readToken() throws Exception {  // process each tooken

        int endIndex;
        String tokenValue;


        while (offset < string.length() && string.charAt(offset) == ' ')  // skip blank
            offset++;

        if (offset >= string.length()) // empty
            return new GeneralType(GeneralType.VOID, null);


        switch(string.charAt(offset)) {

            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '(':
            case ')':
                offset++;
                return new GeneralType(GeneralType.OPERATOR, string.substring(offset - 1, offset));

            case ':': // thing
                StringBuilder stringBuilder = new StringBuilder(string);
                stringBuilder.setCharAt(offset, '\"');
                string = stringBuilder.toString();
                return new GeneralType(GeneralType.OPERATION, ":");

            case '\"': // word
                endIndex = splitUntil(string.substring(offset), " ") + offset;
                tokenValue = string.substring(offset + 1, endIndex);

                offset = endIndex; // reset offset

                if (tokenValue.matches("^[a-z A-Z][_\\w]*$"))
                    return new GeneralType(GeneralType.NAME_WORD, tokenValue);
                else
                    return new GeneralType(GeneralType.NAME_NOT_WORD, tokenValue);

            default:
                endIndex = splitUntil(string.substring(offset), "+-*/%() ") + offset;
                tokenValue = string.substring(offset, endIndex);

                offset = endIndex; // reset offset

                if (tokenValue.matches("^-?[1-9]\\d*(\\.\\d*\\d)?$"))
                    return new GeneralType(GeneralType.NUMBER, tokenValue);
                else if (tokenValue.equals("true") || tokenValue.equals("false"))
                    return new GeneralType(GeneralType.BOOLEAN, tokenValue);
                else
                    return new GeneralType(GeneralType.OPERATION, tokenValue);
        }

    }

    public boolean hasRest() {
        return string.length() > offset;
    }


    private int splitUntil(String string, CharSequence charSequence) {
        for (int i = 0;i < string.length();i++) {
            for (int j = 0;j < charSequence.length(); j++) {
                if (string.charAt(i) == charSequence.charAt(j))
                    return i;
            }
        }
        return string.length();
    }

}
