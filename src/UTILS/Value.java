package UTILS;

import java.util.Vector;

public class Value {

    public enum Type {
        ERROR,
        VOID,
        WORD,
        LIST,
        OPERATION,
        OPERATOR,
    }

    public Type type;
    public String value;
    public Vector<Value> list;


    static public Value word(Value word1, Value word2) { // connect two word
        if (word1.type != Type.WORD || word2.type != Type.WORD)
            return null;
        return new Value(Value.Type.WORD,word1.value + word2.value);
    }

    static public Value sentence(Value value1, Value value2) { // connect two value into list (expand)
        Value newList = new Value(Value.Type.LIST);

        if (value1.type == Type.WORD)
            newList.list.add(new Value(value1));
        else {
            for (Value val : value1.list)
                newList.list.add(new Value(val));
        }
        if (value2.type == Type.WORD)
            newList.list.add(new Value(value2));
        else {
            for (Value val : value2.list)
                newList.list.add(new Value(val));
        }

        return newList;
    }

    static public Value list(Value value1, Value value2) { // connect two value into list (no expand)
        Value newList = new Value(Value.Type.LIST);
        newList.list.add(new Value(value1));
        newList.list.add(new Value(value2));
        return newList;
    }

    static public Value join(Value list, Value value) { // add the value to the list
        if (list.type != Value.Type.LIST) // not list
            return null;

        Value newList = new Value(list);
        newList.list.add(new Value(value));
        return newList;
    }


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
        this.list = new Vector<>();

        for (Value val : value.list)
            this.list.add(new Value(val));
    }


    public boolean isnumber() {
        if (value == null || type == Type.LIST)
            return false;
        else
            return value.matches("-?[1-9]\\d*(.\\d+)?");
    }

    public boolean isword() {
        return type == Type.WORD;
    }

    public boolean islist() {
        return type == Type.LIST;
    }

    public boolean isbool() {
        if (value == null || type == Type.LIST)
            return false;
        else
            return value.equals("true") || value.equals("false");
    }

    public boolean isempty() {
        if (type == Type.LIST)
            return list.size() == 0;
        else
            return value.length() == 0;
    }


    public Value first() {
        if (type == Type.LIST)
            return new Value(list.get(0)); // first element
        else
            return new Value(Type.WORD, String.valueOf(value.charAt(0))); // first charater
    }

    public Value last() {
        if (type == Type.LIST)
            return new Value(list.get(list.size() - 1)); // last element
        else
            return new Value(Type.WORD, String.valueOf(value.charAt(value.length() - 1))); // last charater    }
    }

    public Value butfirst() {
        if (type == Type.LIST) {
            Value newList = new Value(Type.LIST);
            for (int i = 1;i < list.size();i++)
                newList.list.add(new Value(list.get(i))); // except frist element
            return newList;
        } else
            return new Value(Type.WORD, value.substring(0, value.length() - 1)); // except first character
    }

    public Value butlast() {
        if (type == Type.LIST) {
            Value newList = new Value(Type.LIST);
            for (int i = 0;i < list.size() - 1;i++)
                newList.list.add(new Value(list.get(i))); // except last element
            return newList;
        } else
            return new Value(Type.WORD, value.substring(1, value.length())); // except last character
    }
}
