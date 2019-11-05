
package INTERPRETER;

import UTILS.Value;

import java.util.Stack;

public class Calculator {   // for caculate the expression

    private Stack<Value> resStack;
    private Stack<Value> opStack;

    public Calculator() {
        resStack = new Stack<>();
        opStack = new Stack<>();
    }


    public void addValue(Value newValue) throws Exception {

        Value operator;

        if (newValue.type != Value.Type.OPERATOR)
            updateResult(newValue);
        else {
            switch (newValue.value) {
                case "(":
                    resStack.push(newValue);
                    opStack.push(newValue);
                    break;
                case ")":
                    while (!opStack.empty() && !opStack.peek().value.equals("(")) { // pop until find '('
                        operator = opStack.pop();
                        updateResult(operator);
                    }
                    operator = opStack.pop(); // pop '('
                    updateResult(operator);
                    break;

                default:
                    if (resStack.empty() || resStack.peek().type == Value.Type.OPERATOR) // single operand
                        newValue.value = newValue.value + "s";
                    else {
                        int prior = getPriority(newValue); // pop the high priority operator
                        while (!opStack.empty()) {
                            if (prior < getPriority(opStack.peek())) // high priority or right compose
                                break;
                            operator = opStack.pop();
                            updateResult(operator);
                        }
                    }
                    resStack.push(newValue);
                    opStack.push(newValue); // push current operator
                    break;
            }
        }
    }


    public boolean resultFinish() { // it expression finish ?
        return resStack.size() == 1 && opStack.empty();
    }

    public Value getResult() {  // get finial result
        return resStack.pop();
    }

    private int getPriority(Value operator) throws Exception {
        switch (operator.value) {
            case ")":
                return 1;
            case "+s":
            case "-s":
                return 2;
            case "*":
            case "/":
            case "%":
                return 3;
            case "+":
            case "-":
                return 4;
            case ">>":
            case "<<":
                return 5;
            case "<":
            case "<=":
            case ">":
            case ">=":
                return 6;
            case "==":
            case "!=":
                return 7;
            case "&":
                return 8;
            case "^":
                return 9;
            case "|":
                return 10;
            case "&&":
                return 11;
            case "||":
                return 12;
            case "=":
                return 13;
            case "(":
                return Integer.MAX_VALUE;
            default:
                throw new Exception();
        }
    }


    private void updateResult(Value value) throws Exception {
        double resultDouble;
        boolean resultBoolean;
        Value operand1, operand2, operator;

        if (value.type == Value.Type.OPERATOR) { // opeartor
            operator = value;
            switch (operator.value) {  // calculate
                case "+s":
                    operand1 =  resStack.pop();
                    resStack.pop();
                    resStack.push(new Value(Value.Type.WORD, operand1.value));
                    break;

                case "-s":
                    operand1 =  resStack.pop();
                    resStack.pop();
                    if (operand1.value.charAt(0) == '-')
                        operand1.value = operand1.value.substring(1);
                    else
                        operand1.value = "-" + operand1.value;
                    resStack.push(new Value(Value.Type.WORD, operand1.value));
                    break;

                case "+":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultDouble = Double.parseDouble(operand1.value) + Double.parseDouble(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultDouble)));
                    break;

                case "-":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultDouble = Double.parseDouble(operand1.value) - Double.parseDouble(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultDouble)));
                    break;

                case "*":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultDouble = Double.parseDouble(operand1.value) * Double.parseDouble(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultDouble)));
                    break;

                case "/":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultDouble = Double.parseDouble(operand1.value) / Double.parseDouble(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultDouble)));
                    break;

                case "%":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultDouble = Double.parseDouble(operand1.value) % Double.parseDouble(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultDouble)));
                    break;

                case "&&":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = Boolean.parseBoolean(operand1.value) && Boolean.parseBoolean(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "||":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = Boolean.parseBoolean(operand1.value) || Boolean.parseBoolean(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "==":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = operand1.value.equals(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "!=":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = !operand1.value.equals(operand2.value);
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "<":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = operand1.value.compareTo(operand2.value) < 0;
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "<=":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = operand1.value.compareTo(operand2.value) <= 0;
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case ">":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = operand1.value.compareTo(operand2.value) > 0;
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case ">=":
                    operand2 = resStack.pop();
                    resStack.pop();
                    operand1 = resStack.pop();
                    resultBoolean = operand1.value.compareTo(operand2.value) >= 0;
                    resStack.push(new Value(Value.Type.WORD, String.valueOf(resultBoolean)));
                    break;

                case "(":
                    operand1 = resStack.pop();
                    resStack.pop();
                    resStack.push(operand1);
                    break;

                default:
                    throw new Exception();
            }
        } else {
            while (!resStack.empty()) {
                operator = resStack.peek();
                switch (operator.value) {
                    case "+s":
                        resStack.pop();
                        opStack.pop();
                        break;
                    case "-s":
                        resStack.pop();
                        opStack.pop();
                        if (value.value.charAt(0) == '-')
                            value.value = value.value.substring(1);
                        else
                            value.value = "-" + value.value;
                        break;
                    default:
                        resStack.push(value);
                        return;
                }
            }
            resStack.push(value);
        }
    }
}

