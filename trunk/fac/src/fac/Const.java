package fac;

public class Const {

    private final int value;

    public Const(int value) {
        this.value = value & 0xFF;
    }

    public int getValue() {
        return value;
    }

}
