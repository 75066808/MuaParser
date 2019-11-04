package INTERPRETER;

import UTILS.*;
import NAMESPACE.*;
import OPERATION.*;

import java.util.Scanner;

public class Interpreter {

    private Scanner scanner =  new Scanner(System.in);
    private Namespace global = new Namespace(null);


    public void interprete(Namespace namespace, Value list) {

        Namespace current;
        TokenReader reader;
        
        if (namespace == null) // main function
            current = global;
        else // function invocation
            current = namespace;
        
        reader = new TokenReader(scanner, list); // initialize read stream

        while(true) {
            try {
                if (!reader.readEnd())
                    parse(current, reader);
                else 
                    break;
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Value parse(Namespace current, TokenReader reader) throws Exception {

        Value token = reader.readNextToken();

        switch (token.type) {
            case OPERATION:
                return operationParse(current, reader, token);  // start calculate expression
            case OPERATOR:
                if (token.value.equals("("))
                    return expressionParse(current, reader); // start calculate operation
                else
                    throw new Exception();
            default: // word
                return token;
        }

    }


    private Value operationParse(Namespace current, TokenReader reader, Value token) throws Exception {

        Value resultOp1, resultOp2;
        Value resultName, resultValue;
        Value resultNum, resultList;

        switch (token.value) {
            case "//":
                return new Value(Value.Type.VOID, null);

            case "make":
                resultName = parse(current, reader);
                resultValue = parse(current, reader);
                current.setValue(resultName.value, resultValue);
                return new Value(Value.Type.VOID, null);

            case ":":
            case "thing":
                resultName = parse(current, reader);
                resultValue = current.getValue(resultName.value);
                return resultValue;

            case "erase":
                resultName = parse(current, reader);
                current.eraseSymbol(resultName.value);
                return new Value(Value.Type.VOID, null);

            case "isname":
                resultName = parse(current, reader);
                return new Value(Value.Type.BOOLEAN, String.valueOf(current.containSymbol(resultName.value)));

            case "print":
                resultValue = parse(current, reader);
                System.out.println(resultValue.value);
                return new Value(Value.Type.VOID, null);

            case "read":
               return reader.readWordFromInput();

            case "readlist":
                return reader.readListFromInput();

            case "add":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.add(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "sub":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.sub(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "mul":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.mul(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));
            case "div":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.div(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "mod":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.NUMBER,
                        String.valueOf(Operation.mod(Integer.parseInt(resultOp1.value),
                                Integer.parseInt(resultOp2.value))));

            case "eq":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.eq(resultOp1.value,resultOp2.value)));

            case "gt":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.gt(resultOp1.value,resultOp2.value)));

            case "lt":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.lt(resultOp1.value,resultOp2.value)));

            case "and":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.and(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));

            case "or":
                resultOp1 = parse(current, reader);
                resultOp2 = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.or(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));

            case "not":
                resultValue = parse(current, reader);
                return new Value(Value.Type.BOOLEAN,
                        String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));

            case "repeat":
                resultNum = parse(current, reader);
                resultList = parse(current, reader);
                for (int i = 0; i < Integer.parseInt(resultNum.value); i++)
                    interprete(current, resultList);
                return new Value(Value.Type.VOID);

            default:
                throw new Exception();

        }
    }


    private Value expressionParse(Namespace current, TokenReader reader) throws Exception {

        Value token;
        Calculator calculator = new Calculator();
        calculator.addValue(new Value(Value.Type.OPERATOR, "(")); // add first '('

        while (true) {
            if (!reader.readEnd())
                token = reader.readNextToken();
            else
                throw new Exception();

            if (token.type == Value.Type.OPERATION)
                calculator.addValue(operationParse(current, reader, token)); // return from some operation
            else { // number or operator or wrong type
                calculator.addValue(token);
                if (token.value.equals(")") && calculator.resultFinish())
                    return calculator.getResult(); // calculate finish
            }
        }
    }

}
