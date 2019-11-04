package NAMESPACE;
import UTILS.Value;

import java.util.HashMap;

public class Namespace {

    private HashMap<String, Value> symbolMap;
    private Namespace parent;

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
        return symbolMap.get(symbol); // get symbol value
    }

    public boolean containSymbol(String symbol) {
        return symbolMap.containsKey(symbol); // judge whether contains
    }

    public void eraseSymbol(String symbol) {
        symbolMap.remove(symbol); // remove symbol
    }



}
