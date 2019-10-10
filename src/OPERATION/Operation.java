package OPERATION;

import SYMBOL.SymbolList;

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




}
