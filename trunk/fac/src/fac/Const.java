package fac;

public class Const {

    private final int value;

    public Const(int value) {
        this.value = value & 0xFF;
    }

    public Tuple<Const, Const> multiply(Const c) {
        int r = value * c.value;
        return new Tuple(new Const(r), new Const(r >> 8));
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isOne() {
        return value == 1;
    }

    @Override
    public String toString() {
        return Integer.toHexString(value & 0xFF);
    }

}
