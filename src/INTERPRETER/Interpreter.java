package INTERPRETER;

import UTILS.*;
import SYMBOL.*;
import OPERATION.*;

import java.util.Scanner;

public class Interpreter {

    Scanner scan =  new Scanner(System.in);
    SymbolList symList = new SymbolList();


    public void interprete() {

        while(true) {
            try {
                if (scan.hasNextLine()) {
                    LineReader lineReader = new LineReader(scan.nextLine());
                    parse(scan, lineReader);
                } else {
                    break;
                }

            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    private GeneralType parse(Scanner scan, LineReader lineReader) throws Exception {

        GeneralType token = lineReader.readToken();

        switch (token.type) {
            case GeneralType.OPERATION:
                return operationParse(scan, lineReader, token);  // start calculate expression
            case GeneralType.OPERATOR:
                if (token.value.equals("("))
                    return expressionParse(scan, lineReader); // start calculate operation
                else
                    throw new Exception();
            default: // word
                return token;
        }

    }


    private GeneralType operationParse(Scanner scan, LineReader lineReader, GeneralType token) throws Exception {

        GeneralType nextToken;
        GeneralType resultOp1, resultOp2;
        GeneralType resultName, resultValue;

        switch (token.value) {
            case "//":
                return new GeneralType(GeneralType.VOID, null);

            case "make":
                resultName = parse(scan, lineReader);
                resultValue = parse(scan, lineReader);
                symList.setValue(resultName.value, resultValue);
                return new GeneralType(GeneralType.VOID, null);


            case ":":
            case "thing":
                resultName = parse(scan, lineReader);
                resultValue = symList.getValue(resultName.value);
                return resultValue;

            case "erase":
                resultName = parse(scan, lineReader);
                symList.eraseSymbol(resultName.value);
                return new GeneralType(GeneralType.VOID, null);

            case "isname":
                resultName = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN, String.valueOf(symList.containSymbol(resultName.value)));

            case "print":
                resultValue = parse(scan, lineReader);
                System.out.println(resultValue.value);
                return new GeneralType(GeneralType.VOID, null);


            case "read":
                LineReader subReader = new LineReader(scan.nextLine());
                nextToken = subReader.readToken();
                if (subReader.hasRest())
                    return new GeneralType(GeneralType.ERROR, null);
                else
                    return nextToken;

            case "add":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.NUMBER,
                        String.valueOf(Operation.add(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "sub":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.NUMBER,
                        String.valueOf(Operation.sub(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));


            case "mul":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.NUMBER,
                        String.valueOf(Operation.mul(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));
            case "div":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.NUMBER,
                        String.valueOf(Operation.div(Double.parseDouble(resultOp1.value),
                                Double.parseDouble(resultOp2.value))));

            case "mod":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.NUMBER,
                        String.valueOf(Operation.mod(Integer.parseInt(resultOp1.value),
                                Integer.parseInt(resultOp2.value))));

            case "eq":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.eq(resultOp1.value,resultOp2.value)));

            case "gt":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.gt(resultOp1.value,resultOp2.value)));

            case "lt":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.lt(resultOp1.value,resultOp2.value)));


            case "and":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.and(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));

            case "or":
                resultOp1 = parse(scan, lineReader);
                resultOp2 = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.or(Boolean.parseBoolean(resultOp1.value),
                                Boolean.parseBoolean(resultOp2.value))));


            case "not":
                resultValue = parse(scan, lineReader);
                return new GeneralType(GeneralType.BOOLEAN,
                        String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));

            default:
                throw new Exception();

        }
    }


    private GeneralType expressionParse(Scanner scan, LineReader lineReader) throws Exception {

        GeneralType token;
        Calculator calculator = new Calculator();
        calculator.addSymbol(new GeneralType(GeneralType.OPERATOR, "(")); // add first '('

        while (true) {
            if (lineReader.hasRest())
                token = lineReader.readToken();
            else
                throw new Exception();

            if (token.type == GeneralType.OPERATION)
                calculator.addSymbol(operationParse(scan, lineReader, token)); // return from some operation
            else { // number or operator or wrong type
                calculator.addSymbol(token);
                if (token.value.equals(")") && calculator.resultFinish())
                    return calculator.getResult(); // calculate finish
            }
        }
    }

}
