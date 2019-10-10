package UTILS;

public class GeneralType {


    public static final int VOID = 0;
    public static final int NAME = 1;
    public static final int WORD = 2;
    public static final int NUMBER = 3;
    public static final int LIST = 4;
    public static final int BOOLEAN = 5;


    public int type;
    public String value;


    public GeneralType(int type, String value) {
        this.type = type;
        this.value = value;
    }
}
