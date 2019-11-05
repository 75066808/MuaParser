package INTERPRETER;
import UTILS.*;
import java.util.Scanner;

public class TokenReader {

    private boolean stop;

    private Scanner scanner;
    private Value list;
    private int listOffset;

    private StringReader command;
    

    public TokenReader(Scanner scanner, Value list) {
        this.stop = false;
        this.scanner = scanner;
        this.list = list;
        this.listOffset = -1;
        this.command = new StringReader();
    }

    public Value readNextToken() { // read next token

        if (readEnd()) // reach end
            return new Value(Value.Type.ERROR);

        if (command.isEmpty()) {  // string empty
            if (list == null)  // read from scanner
                command.reset(scanner.nextLine()); // reset next string
            else { // read from list
                listOffset++;
                if (list.list.get(listOffset).type == Value.Type.LIST)   // list type
                    return new Value(list.list.get(listOffset)); // directly return
                else 
                    command.reset(list.list.get(listOffset).value); // reset next command line
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
        Value value = new Value(Value.Type.LIST);
        StringReader stream = new StringReader();
        stream.reset(scanner.nextLine());

        while (true) {
            stream.skip(); // skip blank
            if (stream.isEmpty())
                return value; // read end
            if (stream.getChar() == '[') {
                stream.forward();
                value.list.add(readList(stream, false)); // read list from stream
            } else
                value.list.add(new Value(Value.Type.WORD, stream.readUntil("[] "))); // add to list
        }
    }

    public boolean readEnd() {
        if (stop)
            return true;
        else if (!command.isEmpty()) // current string has rest
            return false;
        else if (list == null)  // read from scanner
            return !scanner.hasNextLine();
        else // read list end
            return list.list.size() <= listOffset + 1;
    }

    public void readStopAll() { // stop all read
        stop = true;
    }

    public void readStopLine() { // stop one line ( annotation )
        if (list == null) // read from standard input
            command.stop();
        else // read from list
            readStopAll();
    }

    private Value readToken() {
        command.skip();  // skip blank
        if (command.isEmpty()) // empty
            return new Value(Value.Type.VOID, null);

        switch(command.getChar()) {
            case '+':
            case '-':
            case '*':
            case '%':
            case '^':
            case '(':
            case ')': // single operator
                return new Value(Value.Type.OPERATOR, String.valueOf(command.readChar()));

            case '/': // double operator
                String result = command.readTry("//");
                if (result.equals("//")) // annotation
                    return new Value(Value.Type.OPERATION, "//");
                else // divide
                    return new Value(Value.Type.OPERATOR, "/");
            case '&': // & or &&
                return new Value(Value.Type.OPERATOR, command.readTry("&&"));
            case '|': // | or ||
                return new Value(Value.Type.OPERATOR, command.readTry("||"));
            case '=': // = or ==
                return new Value(Value.Type.OPERATOR, command.readTry("=="));
            case '!': // ! or !=
                return new Value(Value.Type.OPERATOR, command.readTry("!="));

            case '>':
                if (command.readOK(">>")) // >>
                    return new Value(Value.Type.OPERATOR, command.readTry(">>"));
                else // >= or >
                    return new Value(Value.Type.OPERATOR, command.readTry(">="));

            case '<':
                if (command.readOK("<<")) // <<
                    return new Value(Value.Type.OPERATOR, command.readTry("<<"));
                else // <= or <
                    return new Value(Value.Type.OPERATOR, command.readTry("<="));

            case '[': // list
                command.forward();
                return readList(command, true);

            case ':': // thing
                command.setChar('\"');
                return new Value(Value.Type.OPERATION, ":");

            case '\"': // word literal
                command.forward();
                return new Value(Value.Type.WORD, command.readUntil(" "));
            default:
                String token = command.readUntil("+-*/%()&|^!=<> ");
                if (token.matches("-?[1-9]\\d*(.\\d+)?"))
                    return new Value(Value.Type.WORD, token);
                else if (token.equals("true") || token.equals("false"))
                    return new Value(Value.Type.WORD, token);
                return new Value(Value.Type.OPERATION, token);
        }

    }

    private Value readList(StringReader stringReader, boolean fromCommand) {
        Value value = new Value(Value.Type.LIST);

        while (true) {
            stringReader.skip(); // skip blank
            
            if (stringReader.isEmpty()) { // need next line
                if (fromCommand) { // read from command
                    if (readEnd()) 
                        return new Value(Value.Type.ERROR); // reach end
                    stringReader.reset(scanner.nextLine());
                }
                else  // read from stream
                    return new Value(Value.Type.ERROR); // reach end
            }

            switch (stringReader.getChar()) {
                case '[':
                    stringReader.forward();
                    value.list.add(readList(stringReader, fromCommand));
                    break;
                case ']':
                    stringReader.forward();
                    return value;
                default:
                    value.list.add(new Value(Value.Type.WORD, stringReader.readUntil("[] "))); // add to list
                    break;
            }
        }
    }
}
