package INTERPRETER;
import UTILS.Value;

import java.util.Scanner;

public class TokenReader {

    private Scanner scanner;

    private Value list;
    private int listOffset;

    private String command;
    private int commandOffset;

    private String stream;
    private int streamOffset;
    

    public TokenReader(Scanner scanner, Value list) {
        this.scanner = scanner;
        this.list = list;
        this.listOffset = -1;
        this.command = null;
    }

    public Value readNextToken() { // read next token

        if (readEnd()) // reach end
            return new Value(Value.Type.ERROR);

        if (command == null || commandOffset >= command.length()) {  // string empty
            if (list == null) { // read from scanner
                command = scanner.nextLine().trim().replaceAll("\\s+", " ");
                commandOffset = 0; // reset next string
            } else { // read from list
                listOffset++;
                if (list.list.get(listOffset).type == Value.Type.LIST)   // list type
                    return new Value(list.list.get(listOffset)); // directly return
                else {
                    command = list.list.get(listOffset).value;
                    commandOffset = 0; // reset next command line
                }
            }
        }

        return readToken();
    }

    public Value readWordFromInput() {
        String string = scanner.nextLine().trim().replaceAll("\\s+", " ");
        if (string.contains(" "))
            return new Value(Value.Type.ERROR);
        else
            return new Value(Value.Type.WORD, string);
    }

    public Value readListFromInput() {

        int endOffset;
        Value value = new Value(Value.Type.LIST);

        stream = scanner.nextLine().trim().replaceAll("\\s+", "");
        streamOffset = 0;

        while (true) {
            while (streamOffset < stream.length() && stream.charAt(streamOffset) == ' ')
                streamOffset++; // skip blank

            if (streamOffset >= stream.length())
                return value; // read end

            if (stream.charAt(streamOffset) == '[') {
                streamOffset++;
                value.list.add(readList(false)); // read list from stream
            } else {
                endOffset = readUntil(stream.substring(streamOffset), "[] ") + streamOffset; // read one word
                value.list.add(new Value(Value.Type.WORD, stream.substring(streamOffset, endOffset))); // add to list
                streamOffset = endOffset; // update offset
            }
        }
    }

    public boolean readEnd() {

        if (command != null && commandOffset < command.length()) // current string has rest
            return false;
        else if (list == null)  // read from scanner
            return !scanner.hasNextLine();
        else // read list end
            return list.list.size() <= listOffset + 1;

    }

    private Value readToken() {

        int endOffset;
        String tokenValue;

        while (commandOffset < command.length() && command.charAt(commandOffset) == ' ')  // skip blank
            commandOffset++;

        if (commandOffset >= command.length()) // empty
            return new Value(Value.Type.VOID, null);

        switch(command.charAt(commandOffset)) {
            case '/':
            case '+':
            case '-':
            case '*':
            case '%':
            case '(':
            case ')': // operator
                commandOffset++;
                return new Value(Value.Type.OPERATOR, command.substring(commandOffset - 1, commandOffset));

            case '[': // list
                commandOffset++;
                return readList(true);

            case ':': // thing
                StringBuilder stringBuilder = new StringBuilder(command);
                stringBuilder.setCharAt(commandOffset, '\"');
                command = stringBuilder.toString();
                return new Value(Value.Type.OPERATION, ":");

            case '\"': // word literal
                endOffset = readUntil(command.substring(commandOffset), " ") + commandOffset;
                tokenValue = command.substring(commandOffset + 1, endOffset); // read until blank

                commandOffset = endOffset; // reset offset
                if (tokenValue.matches("^[a-z A-Z][_\\w]*$")) // can be the name
                    return new Value(Value.Type.NAME_WORD, tokenValue);
                else
                    return new Value(Value.Type.NAME_NOT_WORD, tokenValue);

            default:
                endOffset = readUntil(command.substring(commandOffset), "+-*/%() ") + commandOffset;
                tokenValue = command.substring(commandOffset, endOffset); // read until operator
                commandOffset = endOffset; // reset offset
                return new Value(Value.Type.WORD, tokenValue);
        }

    }

    private Value readList(boolean fromCommand) {

        int endOffset;
        Value value = new Value(Value.Type.LIST);

        String string = (fromCommand) ? command : stream;
        int stringOffset = (fromCommand) ? commandOffset : streamOffset;

        while (true) {
            while (stringOffset < string.length() && string.charAt(stringOffset) == ' ')
                stringOffset++; // skip blank

            if (stringOffset >= string.length()) { // need next line
                if (fromCommand) { // read from command
                    if (readEnd()) {
                        command = string;
                        commandOffset = stringOffset;
                        return new Value(Value.Type.ERROR); // reach end
                    }
                    string = scanner.nextLine().trim().replaceAll("\\s+", " ");
                    stringOffset = 0;
                }
                else { // read from stream
                    stream = string;
                    streamOffset = stringOffset;
                    return new Value(Value.Type.ERROR); // reach end
                }

            }

            switch (string.charAt(stringOffset)) {
                case '[':
                    stringOffset++;
                    value.list.add(readList(fromCommand));
                    break;

                case ']':
                    stringOffset++;
                    if (fromCommand) { // read from command
                        command = string;
                        commandOffset = stringOffset;
                    } else { // read from stream
                        stream = string;
                        streamOffset = stringOffset;
                    }
                    return value;

                default:
                    endOffset = readUntil(string.substring(stringOffset), "[] ") + stringOffset; // read one word
                    value.list.add(new Value(Value.Type.WORD, string.substring(stringOffset, endOffset))); // add to list
                    stringOffset = endOffset; // update offset
                    break;
            }
        }
    }

    private int readUntil(String string, CharSequence charSequence) {
        for (int i = 0;i < string.length();i++)
            for (int j = 0;j < charSequence.length(); j++)
                if (string.charAt(i) == charSequence.charAt(j))
                    return i;

        return string.length();
    }
}
