package bool.int32;

public class Const implements Expression {

    private final int value;

    public Const(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public SCNF toSCNF() {
        return new SCNF(new SimpleConjunction(this));
    }

    public boolean isZero() {
        return value == 0;
    }

    public Const rotate(int rotate) {
        return new Const(value << rotate | value >> (32 - rotate));
    }

    public Const invert() {
        return new Const(value ^ 0xFFFFFFFF);
    }

    @Override
    public String toString() {
        return Integer.toHexString(value);
    }

}
