package bool.int32;

public class Const implements Expression {

    private int value;

    public Const(int value) {
        this.value = value;
    }

    public Expression invert() {
        return new Const(value ^ 0xFFFFFFFF);
    }

    public String toString() {
        return Integer.toHexString(value);
    }

}
