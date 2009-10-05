package bool.int32;

public class Const implements Expression {

    private final int value;

    public Const(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isFalse() {
        return value == 0;
    }

    public boolean isTrue() {
        return value == 0xFFFFFFFF;
    }

    public Const invert() {
        return new Const(value ^ 0xFFFFFFFF);
    }

    public Const rotate(int rotate) {
        return new Const(value << rotate | value >> (32 - rotate));
    }

    @Override
    public String toString() {
        return Integer.toHexString(value);
    }

    public Expression optimize() {
        return this;
    }

}
