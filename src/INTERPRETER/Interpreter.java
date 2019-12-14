package INTERPRETER;

import UTILS.*;
import ENVIRONMENT.*;
import OPERATION.*;

import java.util.Scanner;
import java.util.Stack;

public class Interpreter {

    private Scanner scanner = new Scanner(System.in);

    private DataSpace global = new DataSpace(null);
    private StringReader command = new StringReader();


    public Value interpret(DataSpace dataSpace, CodeSpace codeSpace) { // interpret whole  
        DataSpace curDataSpace;
        CodeSpace curCodeSpace;

        if (dataSpace == null) { // main function
            curDataSpace = global;
            curCodeSpace = new CodeSpace(scanner);
        }
        else { // function invocation
            curDataSpace = dataSpace;
            curCodeSpace = codeSpace;
        }

        try {
            while(true) {
                if (curDataSpace.isStop()) // already stop
                    return curDataSpace.getReturnValue();

                if (command.empty() && curCodeSpace.readEnd()) // no rest code
                    return curDataSpace.getReturnValue();  // return value

                interpretValue(curDataSpace, curCodeSpace); // interpret value for each command
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Value interpretValue(DataSpace dataSpace, CodeSpace codeSpace) throws Exception { // interpret a value from a command
        String string;

        if (dataSpace.isStop()) // function stop
            return new Value(Value.Type.ERROR);

        command.skip();
        if (command.empty() && codeSpace.readEnd()) // no next code
            return new Value(Value.Type.ERROR);

        if (command.empty()) { // read next line
            Value value = codeSpace.readNextLine();
            if (value.islist())
                return value;
            else
                command.reset(value.value);
        }

        switch(command.getChar()) {
            case '/':  // annoation
                string = command.readString("//");
                if (string == null)
                    throw new Exception();
                command.stop();
                codeSpace.readStopLine();
                return new Value(Value.Type.VOID);

            case '\"': // word literal
                command.forward();
                string = command.readString("[^ ]+");
                if (string == null)
                    throw new Exception();
                else
                    return new Value(Value.Type.WORD, string);

            case '[':  // list
                return interpretList(dataSpace, codeSpace);
            case '(':  // expression
                return interpretExpression(dataSpace, codeSpace);

            case '+':  // number
            case '-':
                string = command.readString("[\\+-]?\\d+(\\.\\d+)?");
                if (string == null)
                    throw new Exception();
                else
                    return new Value(Value.Type.WORD, string);

            default:
                string = command.readString("[^+-/*%() ]+");
                if (string == null)
                    throw new Exception();
                else if (string.matches("\\d+(\\.\\d+)?")) // number
                    return new Value(Value.Type.WORD, string);
                else if (string.equals("true") || string.equals("false")) // boolean
                    return new Value(Value.Type.WORD, string);
                else // operation or function
                    return interpretOperation(dataSpace, codeSpace, string);
        }
    }

    private Value interpretOperation(DataSpace dataSpace, CodeSpace codeSpace, String operation) throws Exception { // interpret an operation
        Value resultWord1, resultWord2;
        Value resultList1, resultList2;
        Value resultValue1, resultValue2;
        Value resultName, resultValue, resultNum, resultList;


        if (operation.charAt(0) == ':')
            return dataSpace.getValue(operation.substring(1)); // name -> value

        switch (operation) { // function
            case "make":
                resultName = interpretValue(dataSpace, codeSpace);
                resultValue = interpretValue(dataSpace, codeSpace);
                dataSpace.setValue(resultName.value, resultValue);
                return new Value(Value.Type.VOID);

            case "thing":
                resultName = interpretValue(dataSpace, codeSpace);
                resultValue = dataSpace.getValue(resultName.value);
                return resultValue;

            case "erase":
                resultName = interpretValue(dataSpace, codeSpace);
                dataSpace.eraseName(resultName.value);
                return new Value(Value.Type.VOID);

            case "isname":
                resultName = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD, String.valueOf(dataSpace.containName(resultName.value)));

            case "print":
                resultValue = interpretValue(dataSpace, codeSpace);
                Operation.print(resultValue);
                System.out.println();
                return new Value(Value.Type.VOID);

            case "read":
                return new Value(Value.Type.WORD,
                        scanner.nextLine().trim().replaceAll("\\s+", " "));

            case "readlist":
                resultValue = new Value(Value.Type.LIST);
                StringReader stream = new StringReader(scanner.nextLine());

                while (true) {
                    stream.skip(); // skip blank
                    if (stream.empty())
                        return resultValue; // read end
                    resultValue.list.add(new Value(Value.Type.WORD, stream.readString("[^ ]+"))); // add to list
                }

            case "add":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.add(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "sub":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.sub(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "mul":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.mul(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));
            case "div":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.div(Double.parseDouble(resultValue1.value),
                                Double.parseDouble(resultValue2.value))));

            case "mod":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.mod(Integer.parseInt(resultValue1.value),
                                Integer.parseInt(resultValue2.value))));

            case "eq":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.eq(resultValue1.value,resultValue2.value)));

            case "gt":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.gt(resultValue1.value,resultValue2.value)));

            case "lt":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.lt(resultValue1.value,resultValue2.value)));

            case "and":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.and(Boolean.parseBoolean(resultValue1.value),
                                Boolean.parseBoolean(resultValue2.value))));

            case "or":
                resultValue1 = interpretValue(dataSpace, codeSpace);
                resultValue2 = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.or(Boolean.parseBoolean(resultValue1.value),
                                Boolean.parseBoolean(resultValue2.value))));

            case "not":
                resultValue = interpretValue(dataSpace, codeSpace);
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));

            case "isnumber":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isnumber()));

            case "isword":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isword()));

            case "islist":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.islist()));

            case "isbool":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isbool()));

            case "isempty":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD, String.valueOf(resultValue.isempty()));

            case "random":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.random(Integer.parseInt(resultValue.value))));

            case "sqrt":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.sqrt(Double.parseDouble(resultValue.value))));

            case "int":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return new Value(Value.Type.WORD,
                        String.valueOf(Operation.floor(Double.parseDouble(resultValue.value))));
            case "word":
                resultWord1 = interpretValue(dataSpace, codeSpace); // get first word
                resultWord2 = interpretValue(dataSpace, codeSpace); // get second word
                return Value.word(resultWord1, resultWord2);

            case "sentence":
                resultValue1 = interpretValue(dataSpace, codeSpace); // get first value
                resultValue2 = interpretValue(dataSpace, codeSpace); // get second value
                return Value.sentence(resultValue1, resultValue2);

            case "list":
                resultValue1 = interpretValue(dataSpace, codeSpace); // get first word
                resultValue2 = interpretValue(dataSpace, codeSpace); // get second word
                return Value.list(resultValue1, resultValue2);

            case "join":
                resultList = interpretValue(dataSpace, codeSpace); // get list
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return Value.join(resultList, resultValue);

            case "first":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return resultValue.first();

            case "last":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return resultValue.last();

            case "butfirst":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return resultValue.butfirst();

            case "butlast":
                resultValue = interpretValue(dataSpace, codeSpace); // get value
                return resultValue.butlast();

            case "if":
                resultValue = interpretValue(dataSpace, codeSpace); // get boolean
                resultList1 = interpretValue(dataSpace, codeSpace); // get first list
                resultList2 = interpretValue(dataSpace, codeSpace); // get second list
                if (Boolean.parseBoolean(resultValue.value)) // judge condition
                    interpret(dataSpace, new CodeSpace(resultList1)); // interpret list1
                else
                    interpret(dataSpace, new CodeSpace(resultList2)); // interpret list2
                return new Value(Value.Type.VOID);
                        
            case "repeat":
                resultNum = interpretValue(dataSpace, codeSpace); // get times
                resultList = interpretValue(dataSpace, codeSpace); // get command
                for (int i = 0; i < Integer.parseInt(resultNum.value); i++)
                    interpret(dataSpace, new CodeSpace(resultList));
                return new Value(Value.Type.VOID);
                
            case "stop":
                dataSpace.setStop();
                return new Value(Value.Type.VOID);

            case "output":
                resultValue = interpretValue(dataSpace, codeSpace); // get return value
                dataSpace.setReturnValue(resultValue); // set return value
                return new Value(Value.Type.VOID);

            case "export":
                resultName = interpretValue(dataSpace, codeSpace); // get name
                global.setValue(resultName.value, dataSpace.getValue(resultName.value)); // export to global namspace
                return new Value(Value.Type.VOID);

            case "wait":
                resultNum = interpretValue(dataSpace, codeSpace); // get time
                Operation.wait(Integer.parseInt(resultNum.value)); // wait
                return new Value(Value.Type.VOID);

            case "save":
                resultName = interpretValue(dataSpace, codeSpace); // get name
                dataSpace.save(resultName.value); // save  
                return new Value(Value.Type.VOID);

            case "load":
                resultName = interpretValue(dataSpace, codeSpace); // get name
                dataSpace.load(resultName.value); // load  
                return new Value(Value.Type.VOID);

            case "erall":
                dataSpace.erall(); // erase all name
                return new Value(Value.Type.VOID);

            case "poall":
                dataSpace.poall(); // print all name
                return new Value(Value.Type.VOID);

            default: // function call
                DataSpace defineSpace = dataSpace.getSpace(operation); // find define data space
                if (defineSpace == null)
                    throw new Exception();
                DataSpace nextSpace = new DataSpace(defineSpace); // create next data space
                resultValue = defineSpace.getValue(operation);  // get value
                resultList1 = resultValue.list.get(0); // get args
                resultList2 = resultValue.list.get(1); // get command

                for (int i = 0;i < resultList1.list.size();i++) // set args
                    nextSpace.setValue(resultList1.list.get(i).value, interpretValue(dataSpace, codeSpace));
                return interpret(nextSpace, new CodeSpace(resultList2)); // run command

        }
    }

    private Value interpretExpression(DataSpace dataSpace, CodeSpace codeSpace) throws Exception { // interpret an expression

        Stack<Value> resStack = new Stack<>();
        Stack<String> opStack = new Stack<>();

        boolean isOperator = true; // true when current is operator, false when value

        command.forward();
        while (true) {
            command.skip();
            if (command.empty() && codeSpace.readEnd()) // no next code
                throw new Exception();

            if (command.empty()) { // read next line
                Value value = codeSpace.readNextLine();
                if (value.islist())
                    throw new Exception();
                else
                    command.reset(value.value);
            }

            if (isOperator) { // current is operator, want to get an operand
                isOperator = false;
                Value value = interpretValue(dataSpace, codeSpace); // get operand value
                if (!value.isnumber())
                    throw new Exception();
                resStack.push(value);
            } else {  // current is operand, want to get an operator
                char operator = command.readChar();
                switch(operator) {
                    case '/':
                        String string = command.readString("//");
                        if (string != null) { // annoation
                            command.stop();
                            codeSpace.readStopLine();
                            continue;
                        }
                    case '+':
                    case '-':
                    case '*':
                    case '%':
                    case ')':
                        isOperator = true;
                        int priority = interpretPriority(String.valueOf(operator));
                        while (true) {
                            if (opStack.empty())
                                break;
                            if (interpretPriority(opStack.peek()) > priority)
                                break;
                            String op = opStack.pop();
                            Value operand2 = resStack.pop();
                            Value operand1 = resStack.pop();
                            resStack.push(interpretBinary(operand1, operand2, op));
                        }
                        if (operator == ')') // finish calculating
                            return resStack.pop();
                        else
                            opStack.push(String.valueOf(operator));
                        break;
                    default:
                        throw new Exception();
                }
            }
        }
    }

    private Value interpretList(DataSpace dataSpace, CodeSpace codeSpace) throws Exception { // interpret a list

        Value value = new Value(Value.Type.LIST);

        command.forward();
        while (true) {
            command.skip(); // skip blank

            if (command.empty() && codeSpace.readEnd()) // no next code
                throw new Exception();

            if (command.empty()) // read next line
                command.reset(codeSpace.readNextLine().value);

            switch (command.getChar()) {
                case '[':
                    value.list.add(interpretList(dataSpace, codeSpace));
                    break;
                case ']':
                    command.forward();
                    return value;
                default:
                    String string = command.readString("[^\\[\\] ]+");
                    if (string == null)
                        throw new Exception();
                    else
                        value.list.add(new Value(Value.Type.WORD, string)); // add to list
                    break;
            }
        }
    }

    private Value interpretBinary(Value operand1, Value operand2, String opeartor) throws Exception {

        double result;
        switch (opeartor) {
            case "+":
                result = Double.parseDouble(operand1.value) + Double.parseDouble(operand2.value);
                break;
            case "-":
                result = Double.parseDouble(operand1.value) - Double.parseDouble(operand2.value);
                break;
            case "*":
                result = Double.parseDouble(operand1.value) * Double.parseDouble(operand2.value);
                break;
            case "/":
                result = Double.parseDouble(operand1.value) / Double.parseDouble(operand2.value);
                break;
            case "%":
                result = Double.parseDouble(operand1.value) % Double.parseDouble(operand2.value);
                break;
            default:
                throw new Exception();
        }

        return new Value(Value.Type.WORD, String.valueOf(result));
    }

    private int interpretPriority(String operator) throws Exception {
        switch (operator) {
            case "*":
            case "/":
            case "%":
                return 3;
            case "+":
            case "-":
                return 4;
            case ")":
                return Integer.MAX_VALUE;
            default:
                throw new Exception();
        }
    }
}
