package NAMESPACE;
import UTILS.Value;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Namespace {

    private HashMap<String, Value> symbolMap;
    private Namespace parent;
    private Value returnValue;


    public Namespace(Namespace parent) {
        this.symbolMap = new HashMap<>(); // init an empty map
        this.parent = parent; // set parent
        setValue("pi", new Value(Value.Type.WORD, "init")); // initial name
        setValue("run", new Value(Value.Type.WORD, "init")); // initial name
    }

    public boolean containName(String name) {
        return symbolMap.containsKey(name); // judge whether contains
    }

    public void eraseName(String name) {
        symbolMap.remove(name); // remove symbol
    }

    public void setValue(String name, Value value) {
        symbolMap.put(name, value); // set symbol value
    }

    public Value getValue(String name) {
        if (containName(name))
            return symbolMap.get(name);   // first get local value
        else if (parent != null)
            return parent.getValue(name); // if not, get parent value
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
        if (containName(symbol))
            return this;   // first find local
        else if (parent != null)
            return parent.getNamespace(symbol); // if not, find parent
        else // not find
            return null;
    }

    public void save(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists())
                 file.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for (Map.Entry<String,Value> entry : symbolMap.entrySet()) {
                bufferedWriter.write(entry.getKey() + " "); // save name
                saveValue(bufferedWriter, entry.getValue()); // save value
            }
            bufferedWriter.close();
        } catch(Exception e) {

        }
    }

    public void load(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            String name;
            Value value;

            for (Map.Entry<String,Value> entry : symbolMap.entrySet()) {
                name = scanner.next(); // load name
                value = loadValue(scanner); // load value
                setValue(name, value); // set value
            }
            scanner.close();
        } catch(Exception e) {

        }
    }

    public void erall() {
        symbolMap.clear();
    }

    public void poall() {
        for (String name : symbolMap.keySet())
            System.out.println(name); // print each name
    }

    private void saveValue(BufferedWriter bufferedWriter, Value value) throws Exception{
        bufferedWriter.write(value.type.toString() + " ");
        if (value.type != Value.Type.LIST) // word
            bufferedWriter.write(value.value + " "); // write word
        else { // list
            bufferedWriter.write(value.list.size() + " "); // wirte size
            for (Value val : value.list)  // write each value
                saveValue(bufferedWriter, val);
        }
    }

    private Value loadValue(Scanner scanner) {
        String type = scanner.next();
        if (!type.equals("LIST"))
            return new Value(Value.Type.WORD, scanner.next()); // load word
        else {
            Value list = new Value(Value.Type.LIST);
            int size = scanner.nextInt(); // get size
            for (int i = 0;i < size;i++)
                list.list.add(loadValue(scanner)); // load each value
            return list;
        }

    }

}
