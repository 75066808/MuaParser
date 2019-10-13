package INTERPRETER;

import UTILS.GeneralType;

import java.util.Stack;

public class Calculator {   // for caculate the expression

    private boolean startCal;
    private Stack<String> resStack;
    private Stack<String> opStack;

    public Calculator() {
        startCal = false;
        resStack = new Stack<>();
        opStack = new Stack<>();
    }

    public void addSymbol(GeneralType newSymbol) throws Exception {

        String operator;

        startCal = true;
        switch (newSymbol.type) {
            case GeneralType.OPERATOR:
                switch (newSymbol.value) {
                    case "(":
                        opStack.push(newSymbol.value);
                        break;
                    case ")":
                        while (!opStack.empty() && !opStack.peek().equals("(")) { // pop until find '('
                            operator = opStack.pop();
                            updateResult(operator);
                        }

                        if (opStack.empty()) // no '('
                            throw new Exception();
                        else
                            opStack.pop(); // pop '('
                        break;

                    default:
                        int prior = getPriority(newSymbol.value); // pop the high priority operator
                        while (!opStack.empty() && getPriority(opStack.peek()) >= prior) {
                            operator = opStack.pop();
                            updateResult(operator);
                        }
                        opStack.push(newSymbol.value); // push current operator
                        break;
                }
                break;

            case GeneralType.NUMBER:
                resStack.push(newSymbol.value);
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
            return new GeneralType(GeneralType.NUMBER, resStack.pop());
        }
    }

    private int getPriority(String operator) throws Exception {
        switch (operator) {
            case "(":
                return 0;
            case "+":
                return 1;
            case "-":
                return 1;
            case "*":
                return 2;
            case "/":
                return 2;
            case "%":
                return 2;
            default:
                throw new Exception();
        }
    }


    private void updateResult(String operator) throws Exception {

        double result;

        if (resStack.size() < 2) // wrong number
            throw new Exception();

        String op2 = resStack.pop();
        String op1 = resStack.pop();

        switch (operator) {  // calculate
            case "+":
                result = Double.parseDouble(op1) + Double.parseDouble(op2);
                resStack.push(String.valueOf(result));
                break;
            case "-":
                result = Double.parseDouble(op1) - Double.parseDouble(op2);
                resStack.push(String.valueOf(result));
                break;
            case "*":
                result = Double.parseDouble(op1) * Double.parseDouble(op2);
                resStack.push(String.valueOf(result));
                break;
            case "/":
                result = Double.parseDouble(op1) / Double.parseDouble(op2);
                resStack.push(String.valueOf(result));
                break;
            case "%":
                result = Integer.parseInt(op1) % Integer.parseInt(op2);
                resStack.push(String.valueOf((int) result));
                break;
            default:
                throw new Exception();
        }
    }
}

