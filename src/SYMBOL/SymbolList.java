package SYMBOL;
import java.util.HashMap;

public class SymbolList {

    private HashMap<String, Value> symMap;

    public SymbolList() {
        symMap = new HashMap<>();
    }

    public void setValue(String symbol, String value) {
        Value currentVal;

        if (symMap.containsKey(symbol))
            currentVal = symMap.get(symbol);
        else
            currentVal = new Value();


        currentVal.value = value;
        symMap.put(symbol, currentVal);
    }

    public String getValue(String symbol) {
        return symMap.get(symbol).value;
    }

    public boolean containSymbol(String symbol) {
        return symMap.containsKey(symbol);
    }

    public void eraseSymbol(String symbol) {
        symMap.remove(symbol);
    }



}
