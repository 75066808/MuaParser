package INTERPRETER;

import UTILS.*;
import NAMESPACE.*;
import OPERATION.*;

import java.util.Scanner;

public class Interpreter {

    private Scanner scanner =  new Scanner(System.in);
    private Namespace global = new Namespace(null);


    public Value interprete(Namespace namespace, Value list) { // interprete whole frame

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
                interpreteCommand(current, reader);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private Value interpreteCommand(Namespace current, TokenReader reader) throws Exception { // interprete one command

        if (reader.readEnd())
            return new Value(Value.Type.ERROR);

        Value token = reader.readNextToken();
        switch (token.type) {
            case OPERATION:
                return interpreteOperation(current, reader, token);  // start calculate expression
            case OPERATOR:
                if (token.value.equals("("))
                    return interpreteExpression(current, reader); // start calculate operation
                else
                    throw new Exception();
            default: // word
                return token;
        }
    }

    private Value interpreteOperation(Namespace current, TokenReader reader, Value token) throws Exception {

        Value resultOp1, resultOp2;
        Value resultName, resultValue;
        Value resultNum, resultList, resultArg, resultCom;

        switch (token.value) {
            case "//":
                return new Value(Value.Type.VOID);

            case "make":
                resultName = interpreteCommand(current, reader);
                resultValue = interpreteCommand(current, reader);
                current.setValue(resultName.value, resultValue);
                return new Value(Value.Type.VOID);

            case ":":
            case "thing":
                resultName = interpreteCommand(current, reader);
                resultValue = current.getValue(resultName.value);
                return resultValue;

            case "erase":
                resultName = interpreteCommand(current, reader);
                current.eraseSymbol(resultName.value);
                return new Value(Value.Type.VOID);

            case "isname":
                resultName = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN, String.valueOf(current.containSymbol(resultName.value)));

            case "print":
                resultValue = interpreteCommand(current, reader);
                Operation.print(resultValue);
                System.out.println();
                return new Value(Value.Type.VOID);

            case "read":
               return reader.readWordFromInput();

            case "readlist":
                return reader.readListFromInput();

            case "add":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.add(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "sub":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.sub(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "mul":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.mul(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));
            case "div":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.div(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "mod":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.mod(Integer.parseInt(resultOp1.value),
                                Integer.parseInt(resultOp2.value))));

            case "eq":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.eq(resultOp1.value,resultOp2.value)));

            case "gt":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.gt(resultOp1.value,resultOp2.value)));

            case "lt":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.lt(resultOp1.value,resultOp2.value)));

            case "and":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.and(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));

            case "or":
                resultOp1 = interpreteCommand(current, reader);
                resultOp2 = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.or(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));

            case "not":
                resultValue = interpreteCommand(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));

            case "isnumber":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.BOOLEAN, String.valueOf(resultValue.isnumber()));

            case "isword":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.BOOLEAN, String.valueOf(resultValue.isword()));

            case "islist":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.BOOLEAN, String.valueOf(resultValue.islist()));

            case "isbool":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.BOOLEAN, String.valueOf(resultValue.isbool()));

            case "isempty":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.BOOLEAN, String.valueOf(resultValue.isempty()));

            case "random":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.random(Integer.parseInt(resultValue.value))));

            case "sqrt":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.sqrt(Double.parseDouble(resultValue.value))));

            case "int":
                resultValue = interpreteCommand(current, reader); // get value
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.floor(Integer.parseInt(resultValue.value))));

            case "repeat":
                resultNum = interpreteCommand(current, reader);
                resultList = interpreteCommand(current, reader);
                for (int i = 0; i < Integer.parseInt(resultNum.value); i++)
                    interprete(current, resultList);
                return new Value(Value.Type.VOID);

            case "stop":
                reader.readStop();
                return new Value(Value.Type.VOID);

            case "output":
                resultValue = interpreteCommand(current, reader); // get return value
                current.setReturnValue(resultValue); // set return value
                return new Value(Value.Type.VOID);

            case "export":
                resultName = interpreteCommand(current, reader); // get name
                global.setValue(resultName.value, current.getValue(resultName.value)); // export to global namspace
                return new Value(Value.Type.VOID);

            default: // function call
                resultValue = current.getValue(token.value);  // find definition
                resultList = interpreteCommand(current, reader); // get parameter
                if (resultValue == null)
                    throw new Exception();

                Namespace next = new Namespace(current.getNamespace(token.value)); // set namespace
                resultArg = resultValue.list.get(0); // get args
                resultCom = resultValue.list.get(1); // get command

                for (int i = 0;i < resultArg.list.size();i++) // set args
                    next.setValue(resultArg.list.get(i).value, resultList.list.get(i));
                return interprete(next, resultCom);
        }
    }


    private Value interpreteExpression(Namespace current, TokenReader reader) throws Exception {

        Value token;
        Calculator calculator = new Calculator();
        calculator.addValue(new Value(Value.Type.OPERATOR, "(")); // add first '('

        while (true) {
            if (!reader.readEnd())
                token = reader.readNextToken();
            else
                throw new Exception();

            if (token.type == Value.Type.OPERATION)
                calculator.addValue(interpreteOperation(current, reader, token)); // return from some operation
            else { // number or operator or wrong type
                calculator.addValue(token);
                if (token.value.equals(")") && calculator.resultFinish())
                    return calculator.getResult(); // calculate finish
            }
        }
    }

}
