package INTERPRETER;

import UTILS.*;
import NAMESPACE.*;
import OPERATION.*;

import java.util.Scanner;

public class Interpreter {

    private Scanner scanner =  new Scanner(System.in);
    private Namespace global = new Namespace(null);


    public Value interpret(Namespace namespace, Value list) { // interpret whole frame
        Namespace current;
        TokenReader reader;
        
        if (namespace == null) // main function
            current = global;
        else // function invocation
            current = namespace;
        
        reader = new TokenReader(scanner, list); // initialize read stream

        try {
            while(true) {
                if (reader.readEnd())
                    return current.getReturnValue(); // return value;
                interpretCommand(current, reader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Value interpretCommand(Namespace current, TokenReader reader) throws Exception { // interpret one command
        if (reader.readEnd())
            return new Value(Value.Type.ERROR);

        Value token = reader.readNextToken();
        switch (token.type) {
            case OPERATION:
                return interpretOperation(current, reader, token);  // start calculate expression
            case OPERATOR:
                return interpretExpression(current, reader, token); // start calculate operation
            default: // word
                return token;
        }
    }

    private Value interpretOperation(Namespace current, TokenReader reader, Value token) throws Exception {
        Value resultWord1, resultWord2;
        Value resultList1, resultList2;
        Value resultValue1, resultValue2;
        Value resultName, resultValue, resultNum, resultList;

        switch (token.value) {
            case "//":
                reader.readStopLine();
                return new Value(Value.Type.VOID);

            case "make":
                resultName = interpretCommand(current, reader);
                resultValue = interpretCommand(current, reader);
                current.setValue(resultName.value, resultValue);
                return new Value(Value.Type.VOID);

            case ":":
            case "thing":
                resultName = interpretCommand(current, reader);
                resultValue = current.getValue(resultName.value);
                return resultValue;

            case "erase":
                resultName = interpretCommand(current, reader);
                current.eraseName(resultName.value);
                return new Value(Value.Type.VOID);

            case "isname":
                resultName = interpretCommand(current, reader);
                return new Value(Value.Type.WORD, String.valueOf(current.containName(resultName.value)));

            case "print":
                resultValue = interpretCommand(current, reader);
                Operation.print(resultValue);
                System.out.println();
                return new Value(Value.Type.VOID);

            case "read":
               return reader.readWordFromInput();

            case "readlist":
                return reader.readListFromInput();

            case "add":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.add(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "sub":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.sub(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "mul":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.mul(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));
            case "div":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.div(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "mod":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.mod(Integer.parseInt(resultValue1.value),
                                Integer.parseInt(resultValue2.value))));

            case "eq":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.eq(resultValue1.value,resultValue2.value)));

            case "gt":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.gt(resultValue1.value,resultValue2.value)));

            case "lt":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.lt(resultValue1.value,resultValue2.value)));

            case "and":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.and(Boolean.parseBoolean(resultValue1.value),
                                Boolean.parseBoolean(resultValue2.value))));

            case "or":
                resultValue1 = interpretCommand(current, reader);
                resultValue2 = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.or(Boolean.parseBoolean(resultValue1.value),
                                Boolean.parseBoolean(resultValue2.value))));

            case "not":
                resultValue = interpretCommand(current, reader);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));

            case "isnumber":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isnumber()));

            case "isword":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isword()));

            case "islist":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.islist()));

            case "isbool":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isbool()));

            case "isempty":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isempty()));

            case "random":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.random(Integer.parseInt(resultValue.value))));

            case "sqrt":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.sqrt(Double.parseDouble(resultValue.value))));

            case "int":
                resultValue = interpretCommand(current, reader); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.floor(Double.parseDouble(resultValue.value))));
            case "word":
                resultWord1 = interpretCommand(current, reader); // get first word
                resultWord2 = interpretCommand(current, reader); // get second word
                return Value.word(resultWord1, resultWord2);

            case "sentence":
                resultValue1 = interpretCommand(current, reader); // get first value
                resultValue2 = interpretCommand(current, reader); // get second value
                return Value.sentence(resultValue1, resultValue2);

            case "list":
                resultValue1 = interpretCommand(current, reader); // get first word
                resultValue2 = interpretCommand(current, reader); // get second word
                return Value.list(resultValue1, resultValue2);

            case "join":
                resultList = interpretCommand(current, reader); // get list
                resultValue = interpretCommand(current, reader); // get value
                return Value.join(resultList, resultValue);

            case "first":
                resultValue = interpretCommand(current, reader); // get value
                return resultValue.first();

            case "last":
                resultValue = interpretCommand(current, reader); // get value
                return resultValue.last();

            case "butfirst":
                resultValue = interpretCommand(current, reader); // get value
                return resultValue.butfirst();

            case "butlast":
                resultValue = interpretCommand(current, reader); // get value
                return resultValue.butlast();

            case "if":
                resultValue = interpretCommand(current, reader); // get boolean
                resultList1 = interpretCommand(current, reader); // get first list
                resultList2 = interpretCommand(current, reader); // get second list
                if (Boolean.parseBoolean(resultValue.value)) // judge condition
                    interpret(current, resultList1); // interpret list1
                else
                    interpret(current, resultList2); // interpret list2
                return new Value(Value.Type.VOID);
                        
            case "repeat":
                resultNum = interpretCommand(current, reader); // get times
                resultList = interpretCommand(current, reader); // get command
                for (int i = 0; i < Integer.parseInt(resultNum.value); i++)
                    interpret(current, resultList);
                return new Value(Value.Type.VOID);
                
            case "stop":
                reader.readStopAll();
                return new Value(Value.Type.VOID);

            case "output":
                resultValue = interpretCommand(current, reader); // get return value
                current.setReturnValue(resultValue); // set return value
                return new Value(Value.Type.VOID);

            case "export":
                resultName = interpretCommand(current, reader); // get name
                global.setValue(resultName.value, current.getValue(resultName.value)); // export to global namspace
                return new Value(Value.Type.VOID);

            case "wait":
                resultNum = interpretCommand(current, reader); // get time
                Operation.wait(Integer.parseInt(resultNum.value)); // wait
                return new Value(Value.Type.VOID);

            case "save":
                resultName = interpretCommand(current, reader); // get name
                current.save(resultName.value); // save namespace
                return new Value(Value.Type.VOID);

            case "load":
                resultName = interpretCommand(current, reader); // get name
                current.load(resultName.value); // load namespace
                return new Value(Value.Type.VOID);

            case "erall":
                current.erall(); // erase all name
                return new Value(Value.Type.VOID);

            case "poall":
                current.poall(); // print all name
                return new Value(Value.Type.VOID);

            default: // function call
                Namespace define = current.getNamespace(token.value); // find namespace
                if (define == null)
                    throw new Exception();
                if (token.value.equals("pi")) // pi
                    return new Value(Value.Type.WORD, "3.14159");
                else if (token.value.equals("run")) { // run
                    resultList = interpretCommand(current, reader); // get parameter
                    interpret(current, resultList);
                    return new Value(Value.Type.VOID);
                } else {
                    Namespace next = new Namespace(define); // next namespace
                    resultValue = define.getValue(token.value);  // get value
                    resultList = interpretCommand(current, reader); // get parameter
                    resultList1 = resultValue.list.get(0); // get args
                    resultList2 = resultValue.list.get(1); // get command

                    for (int i = 0;i < resultList1.list.size();i++) // set args
                        next.setValue(resultList1.list.get(i).value, resultList.list.get(i));
                    return interpret(next, resultList2); // run command
                }

        }
    }


    private Value interpretExpression(Namespace current, TokenReader reader, Value token) throws Exception {
        Value tokenValue;

        Calculator calculator = new Calculator();
        calculator.addValue(token); // add first

        while (true) {
            if (!reader.readEnd())
                tokenValue = reader.readNextToken();
            else
                throw new Exception();

            if (tokenValue.type == Value.Type.OPERATION)
                calculator.addValue(interpretOperation(current, reader, tokenValue)); // return from some operation
            else { // number or operator or wrong type
                calculator.addValue(tokenValue);
                if (calculator.resultFinish())
                    return calculator.getResult(); // calculate finish
            }
        }
    }

}
