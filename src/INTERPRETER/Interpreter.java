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
                LineReader lineReader = new LineReader(scan.nextLine().split(" "));
                parse(lineReader);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

        }

    }

    private GeneralType parse(LineReader lineReader) throws Exception{

        GeneralType resultOp1, resultOp2;
        GeneralType resultName, resultValue;

        String value;
        String token = lineReader.readString();

        try {
            switch(token) {

                case "//":
                    return new GeneralType(GeneralType.VOID, null);

                case "make":
                    resultName = parse(lineReader);
                    resultValue = parse(lineReader);
                    symList.setValue(resultName.value, resultValue.value);
                    return new GeneralType(GeneralType.VOID, null);

                case "thing":
                    resultName = parse(lineReader);
                    value = symList.getValue(resultName.value);
                    if (value.matches("^-?[1-9]\\d*(\\.\\d*\\d)?$"))
                        return new GeneralType(GeneralType.NUMBER,value);
                    else
                        return new GeneralType(GeneralType.WORD,value);

                case "erase":
                    resultName = parse(lineReader);
                    symList.eraseSymbol(resultName.value);
                    return new GeneralType(GeneralType.VOID, null);

                case "isname":
                    resultName = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN, String.valueOf(symList.containSymbol(resultName.value)));

                case "print":
                    resultValue = parse(lineReader);
                    if (resultValue.type == GeneralType.NUMBER)
                        System.out.println(Double.parseDouble(resultValue.value));
                    else
                        System.out.println(resultValue.value);
                    return new GeneralType(GeneralType.VOID, null);

                case "add":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.NUMBER,
                            String.valueOf(Operation.add(Double.parseDouble(resultOp1.value),
                                    Double.parseDouble(resultOp2.value))));

                case "sub":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.NUMBER,
                            String.valueOf(Operation.sub(Double.parseDouble(resultOp1.value),
                                    Double.parseDouble(resultOp2.value))));


                case "mul":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.NUMBER,
                            String.valueOf(Operation.mul(Double.parseDouble(resultOp1.value),
                                    Double.parseDouble(resultOp2.value))));
                case "div":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.NUMBER,
                            String.valueOf(Operation.div(Double.parseDouble(resultOp1.value),
                                    Double.parseDouble(resultOp2.value))));

                case "mod":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.NUMBER,
                            String.valueOf(Operation.mul(Integer.parseInt(resultOp1.value),
                                    Integer.parseInt(resultOp2.value))));

                case "eq":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.eq(resultOp1.value,resultOp2.value)));

                case "gt":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.gt(resultOp1.value,resultOp2.value)));

                case "lt":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.lt(resultOp1.value,resultOp2.value)));


                case "and":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.and(Boolean.parseBoolean(resultOp1.value),
                                    Boolean.parseBoolean(resultOp2.value))));

                case "or":
                    resultOp1 = parse(lineReader);
                    resultOp2 = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.or(Boolean.parseBoolean(resultOp1.value),
                                    Boolean.parseBoolean(resultOp2.value))));


                case "not":
                    resultValue = parse(lineReader);
                    return new GeneralType(GeneralType.BOOLEAN,
                            String.valueOf(Operation.not(Boolean.parseBoolean(resultValue.value))));


                default:
                    switch(token.charAt(0)) {
                        case '\"':
                            return new GeneralType(GeneralType.WORD, token.substring(1));
                        case ':':
                            if (token.substring(1).matches("^-?[1-9]\\d*(\\.\\d*\\d)?$"))
                                return new GeneralType(GeneralType.NUMBER, symList.getValue(token.substring(1)));
                            else
                                return new GeneralType(GeneralType.WORD, symList.getValue(token.substring(1)));
                        default:
                            if (token.matches("^-?[1-9]\\d*(\\.\\d*\\d)?$"))
                                return new GeneralType(GeneralType.NUMBER, token);
                            else
                                return new GeneralType(GeneralType.NAME, token);
                    }
            }
        } catch (Exception e) {
            throw e;
        }

    }


}
