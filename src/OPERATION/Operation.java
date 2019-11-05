package OPERATION;

import UTILS.Value;

public class Operation {


    static public double add(double value1, double value2) {
        return value1 + value2;
    }

    static public double sub(double value1, double value2) {
        return value1 - value2;
    }

    static public double mul(double value1, double value2) {
        return value1 * value2;
    }

    static public double div(double value1, double value2) {
        return value1 / value2;
    }

    static public int mod(int value1, int value2) {
        return value1 % value2;
    }

    static public boolean eq(String value1, String value2) {
        return value1.compareTo(value2) == 0;
    }

    static public boolean gt(String value1, String value2) {
        return value1.compareTo(value2) > 0;
    }

    static public boolean lt(String value1, String value2) {
        return value1.compareTo(value2) < 0;
    }

    static public boolean and(boolean value1, boolean value2) {
        return value1 && value2;
    }

    static public boolean or(boolean value1, boolean value2) {
        return value1 || value2;
    }

    static public boolean not(boolean value) {
        return !value;
    }


    static public int random(int value) {
        return (int)System.currentTimeMillis() % value;
    }

    static public double sqrt(double value) {
        return Math.sqrt(value);
    }

    static public int floor(double value) {
        return (int)value;
    }

    static public void print(Value value) {
        if (value.type != Value.Type.LIST)
            System.out.print(value.value);
        else {
            System.out.print("[ ");
            for (int i = 0;i < value.list.size(); i++) {
                print(value.list.get(i));
                System.out.print(" ");
            }
            System.out.print("]");
        }

    }


}
