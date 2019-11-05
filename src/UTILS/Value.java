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
        this.value = null;
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


    public boolean isnumber() {
        if (value == null || type == Type.LIST)
            return false;
        else if (type == Type.NUMBER)
            return true;
        else
            return value.matches("-?[1-9]\\d*(.\\d+)?");
    }

    public boolean isword() {
        return type == Type.WORD || type == Type.NUMBER || type == Type.BOOLEAN;
    }

    public boolean islist() {
        return type == Type.LIST;
    }

    public boolean isbool() {
        if (value == null || type == Type.LIST)
            return false;
        else if (type == Type.BOOLEAN)
            return true;
        else
            return value.equals("true") || value.equals("false");
    }

    public boolean isempty() {
        if (type == Type.LIST)
            return list.size() == 0;
        else
            return value.length() == 0;
    }
}
