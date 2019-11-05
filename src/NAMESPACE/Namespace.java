package NAMESPACE;
import UTILS.Value;

import java.util.HashMap;

public class Namespace {

    private HashMap<String, Value> symbolMap;
    private Namespace parent;
    private Value returnValue;


    public Namespace(Namespace parent) {
        this.symbolMap = new HashMap<>(); // init an empty map
        this.parent = parent; // set parent
    }

    public Namespace getParent() {
        return this.parent; // return parent namespace
    }

    public void setValue(String symbol, Value value) {
        symbolMap.put(symbol, value); // set symbol value
    }

    public Value getValue(String symbol) {
        if (containSymbol(symbol))
            return symbolMap.get(symbol);   // first get local value
        else if (parent != null)
            return parent.getValue(symbol); // if not, get parent value
        else // not find
            return null;
    }

    public void setReturnValue(Value value) {
        returnValue = value;
    }

    public Value getReturnValue() {
        return returnValue;
    }

    public Namespace getNamespace(String symbol) {
        if (containSymbol(symbol))
            return this;   // first get local value
        else if (parent != null)
            return parent.getNamespace(symbol); // if not, get parent value
        else // not find
            return null;
    }

    public boolean containSymbol(String symbol) {
        return symbolMap.containsKey(symbol); // judge whether contains
    }

    public void eraseSymbol(String symbol) {
        symbolMap.remove(symbol); // remove symbol
    }



}
