package UTILS;

import java.util.Vector;

public class Value {

    public enum Type {
        ERROR,
        VOID,
        NAME_WORD,
        NAME_NOT_WORD,
        WORD,
        NUMBER,
        LIST,
        BOOLEAN,
        OPERATION,
        OPERATOR,
    }

    public Type type;
    public String value;
    public Vector<Value> list;

    public Value(Type type) { // set type
        this.type = type;
        this.list = new Vector<>();
    }

    public Value(Type type, String value) { // set type and value
        this.type = type;
        this.value = value;
        this.list = new Vector<>();
    }

    public Value(Value value) {  // deep copy other value
        this.type = value.type;
        this.value = value.value;

        for (int i = 0;i < value.list.size(); i++)
            this.list.add(new Value(value.list.get(i)));
    }
}
