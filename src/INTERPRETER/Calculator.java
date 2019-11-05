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
        switch (newValue.type) {
            case OPERATOR:
                switch (newValue.value) {
                    case "(":
                        opStack.push(newValue);
                        break;
                    case ")":
                        while (!opStack.empty() && !opStack.peek().value.equals("(")) { // pop until find '('
                            operator = opStack.pop();
                            updateResult(operator);
                        }
                        if (opStack.empty()) // no '('
                            throw new Exception();
                        opStack.pop(); // pop '('
                        break;
                    default:
                        int priority = getPriority(newValue);
                        while (!opStack.empty()) {
                            if (priority < getPriority(opStack.peek())) // if priority is high
                                break;
                            operator = opStack.pop();
                            updateResult(operator);
                        }
                        opStack.push(newValue);
                        break;
                }
                break;
            default:
                resStack.push(newValue);
                break;
        }
    }


    public boolean resultFinish() { // it expression finish ?
        return resStack.size() == 1 && opStack.empty();
    }

    public Value getResult() {  // get finial result
        if (!resultFinish())
            return new Value(Value.Type.ERROR);
        else
            return resStack.pop();
    }

    private int getPriority(Value operator) throws Exception {
        switch (operator.value) {
            case ")":
                return 1;
            case "*":
            case "/":
            case "%":
                return 3;
            case "+":
            case "-":
                return 4;
            case "(":
                return Integer.MAX_VALUE;
            default:
                throw new Exception();
        }
    }


    private void updateResult(Value operator) throws Exception {

        double result;
        Value operand1, operand2;

        switch (operator.value) {  // calculate
            case "+":
                operand2 =  resStack.pop();
                operand1 = resStack.pop();
                result = Double.parseDouble(operand1.value) + Double.parseDouble(operand2.value);
                resStack.push(new Value(Value.Type.NUMBER, String.valueOf(result)));
                break;

            case "-":
                operand2 =  resStack.pop();
                operand1 = resStack.pop();
                result = Double.parseDouble(operand1.value) - Double.parseDouble(operand2.value);
                resStack.push(new Value(Value.Type.NUMBER, String.valueOf(result)));
                break;

            case "*":
                operand2 =  resStack.pop();
                operand1 = resStack.pop();
                result = Double.parseDouble(operand1.value) * Double.parseDouble(operand2.value);
                resStack.push(new Value(Value.Type.NUMBER, String.valueOf(result)));
                break;

            case "/":
                operand2 =  resStack.pop();
                operand1 = resStack.pop();
                result = Double.parseDouble(operand1.value) / Double.parseDouble(operand2.value);
                resStack.push(new Value(Value.Type.NUMBER, String.valueOf(result)));
                break;

            case "%":
                operand2 =  resStack.pop();
                operand1 = resStack.pop();
                result = Double.parseDouble(operand1.value) % Double.parseDouble(operand2.value);
                resStack.push(new Value(Value.Type.NUMBER, String.valueOf(result)));
                break;

            default:
                throw new Exception();
        }
    }
}

