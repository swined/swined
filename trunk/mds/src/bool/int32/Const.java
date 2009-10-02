package bool.int32;

public class Const implements Expression, VariableOrConst {

    private int value;

    public Const(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public SCNF toSCNF() {
        SimpleConjunction sc = new SimpleConjunction(this);
        return new SCNF(sc);
    }

    public Expression rotate(int rotate) {
        return new Const(value << rotate | value >> (32 - rotate));
    }

    public Expression optimize() {
        return this;
    }

    public Expression invert() {
        return new Const(value ^ 0xFFFFFFFF);
    }

    public String toString() {
        return Integer.toHexString(value);
    }

}
