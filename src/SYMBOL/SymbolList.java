package SYMBOL;
import UTILS.GeneralType;

import java.util.HashMap;

public class SymbolList {

    private HashMap<String, GeneralType> symMap;

    public SymbolList() {
        symMap = new HashMap<>();
    }

    public void setValue(String symbol, GeneralType value) {
        symMap.put(symbol, value);
    }

    public GeneralType getValue(String symbol) {
        return symMap.get(symbol);
    }

    public boolean containSymbol(String symbol) {
        return symMap.containsKey(symbol);
    }

    public void eraseSymbol(String symbol) {
        symMap.remove(symbol);
    }



}
