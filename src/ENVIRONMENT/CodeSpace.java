package ENVIRONMENT;
import UTILS.*;
import java.util.Scanner;

public class CodeSpace {

    private Scanner scanner;
    private Value list;
    private int listOffset;


    public CodeSpace(Scanner scanner) {
        this.list = null;
        this.scanner = scanner;
    }

    public CodeSpace(Value list) {
        this.list = list;
        this.listOffset = -1;
    }

    public Value readNextLine() { // read next token
        if (readEnd()) // reach end
            return new Value(Value.Type.ERROR);

        if (list == null)   // read from scanner
            return new Value(Value.Type.WORD, scanner.nextLine()); // reset next string
        else { // read from list
            listOffset++;
            return list.list.get(listOffset);
        }
    }


    public boolean readEnd() {
        if (list == null)  // read from scanner
            return !scanner.hasNextLine();
        else // read list end
            return list.list.size() <= listOffset + 1;
    }

    public void readStopLine() { // stop one line ( annotation )
        if (list != null)        // read from standard input
            listOffset++;
    }

}
