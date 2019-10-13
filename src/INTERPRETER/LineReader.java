package INTERPRETER;

import UTILS.GeneralType;

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

        if (offset >= string.length()) // empty
            return new GeneralType(GeneralType.VOID, null);

        switch(string.charAt(offset)) {

            case ':': // thing
                StringBuilder stringBuilder = new StringBuilder(string);
                stringBuilder.setCharAt(offset, '\"');
                string = stringBuilder.toString();
                return new GeneralType(GeneralType.OPERATION, ":");

            case '\"': // word
                endIndex = string.indexOf(' ',offset);
                if (endIndex == -1) // not found
                    endIndex = string.length();
                tokenValue = string.substring(offset + 1, endIndex);
                offset = endIndex + 1; // reset offset

                if (tokenValue.matches("^[a-z A-Z][_\\w]*$"))
                    return new GeneralType(GeneralType.NAME_WORD, tokenValue);
                else
                    return new GeneralType(GeneralType.NAME_NOT_WORD, tokenValue);

            default:
                endIndex = string.indexOf(' ',offset);
                if (endIndex == -1) // not found
                    endIndex = string.length();
                tokenValue = string.substring(offset, endIndex);
                offset = endIndex + 1; // reset offset

                if (tokenValue.matches("^-?[1-9]\\d*(\\.\\d*\\d)?$"))
                    return new GeneralType(GeneralType.NUMBER, tokenValue);
                else if (tokenValue.equals("true") || tokenValue.equals("false"))
                    return new GeneralType(GeneralType.BOOLEAN, tokenValue);
                else if (tokenValue.matches("^[\\+\\-\\*/%()]$"))
                    return new GeneralType(GeneralType.OPERATOR, tokenValue);
                else
                    return new GeneralType(GeneralType.OPERATION, tokenValue);
        }

    }

    public boolean hasRest() {
        return string.length() > offset;
    }
}
