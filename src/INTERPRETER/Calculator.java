package INTERPRETER;

import UTILS.GeneralType;

import java.util.Stack;

public class Calculator {   // for caculate the expression

    private boolean startCal;
    private Stack<GeneralType> resStack;
    private Stack<GeneralType> opStack;

    public Calculator() {
        startCal = false;
        resStack = new Stack<>();
        opStack = new Stack<>();
    }


    public void addSymbol(GeneralType newSymbol) throws Exception {

        GeneralType operator;

        startCal = true;

        switch (newSymbol.type) {
            case GeneralType.OPERATOR:
                switch (newSymbol.value) {
                    case "(":
                        resStack.push(newSymbol);
                        opStack.push(newSymbol);
                        break;
                    case ")":
                        while (!opStack.empty() && !opStack.peek().value.equals("(")) { // pop until find '('
                            operator = opStack.pop();
                            updateResult(operator);
                        }
                        if (opStack.empty()) // no '('
                            throw new Exception();

                        operator = opStack.pop(); // pop '('
                        updateResult(operator);
                        break;


                    case "+":
                    case "-":
                    case "*":
                        if (resStack.peek().type == GeneralType.NUMBER)  // double operand
                            newSymbol.value += "d";
                        else  // signal operand
                            newSymbol.value += "s";  //  postpone no break

                    default:
                        int prior = getPriority(newSymbol); // pop the high priority operator

                        while (!opStack.empty()) {
                            int curPiror = getPriority(opStack.peek());

                            if (prior < curPiror || (prior == curPiror && prior == 2)) // high priority or right compose
                                break;

                            operator = opStack.pop();
                            updateResult(operator);
                        }

                        resStack.push(newSymbol);
                        opStack.push(newSymbol); // push current operator
                        break;
                }
                break;

            case GeneralType.NUMBER:
                resStack.push(newSymbol);
                break;

            default:
                startCal = false;
                throw new Exception();
        }
    }


    public boolean resultFinish() { // it expression finish ?
        return startCal && resStack.size() == 1 && opStack.empty();
    }

    public GeneralType getResult() {  // get finial result

        if (!startCal || !resultFinish()) {
            return new GeneralType(GeneralType.ERROR, null);
        } else {
            startCal = false;
            return resStack.pop();
        }
    }

    private int getPriority(GeneralType operator) throws Exception {
        switch (operator.value) {
            case ")":
                return 1;
            case "+s":
            case "-s":
                return 2;
            case "*d":
            case "/":
            case "%":
                return 3;
            case "+d":
            case "-d":
                return 4;
            case "(":
                return Integer.MAX_VALUE;
            default:
                throw new Exception();
        }
    }


    private void updateResult(GeneralType operator) throws Exception {

        double result;
        GeneralType operand1, operand2, op;


        switch (operator.value) {  // calculate
            case "+s":
                operand1 =  resStack.pop();
                op = resStack.pop();

                result = Double.parseDouble(operand1.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "-s":
                operand1 =  resStack.pop();
                op = resStack.pop();

                result = - Double.parseDouble(operand1.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "+d":
                operand2 =  resStack.pop();
                op = resStack.pop();
                operand1 = resStack.pop();

                result = Double.parseDouble(operand1.value) + Double.parseDouble(operand2.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "-d":
                operand2 =  resStack.pop();
                op = resStack.pop();
                operand1 = resStack.pop();

                result = Double.parseDouble(operand1.value) - Double.parseDouble(operand2.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "*d":
                operand2 =  resStack.pop();
                op = resStack.pop();
                operand1 = resStack.pop();

                result = Double.parseDouble(operand1.value) * Double.parseDouble(operand2.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "/":
                operand2 =  resStack.pop();
                op = resStack.pop();
                operand1 = resStack.pop();

                result = Double.parseDouble(operand1.value) / Double.parseDouble(operand2.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "%":
                operand2 =  resStack.pop();
                op = resStack.pop();
                operand1 = resStack.pop();

                result = Double.parseDouble(operand1.value) % Double.parseDouble(operand2.value);
                resStack.push(new GeneralType(GeneralType.NUMBER, String.valueOf(result)));
                break;

            case "(":
                operand1 = resStack.pop();
                op = resStack.pop();
                resStack.push(operand1);
                break;

            default:
                throw new Exception();
        }
    }
}

