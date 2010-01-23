package mds1;

public class Or1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;

    public Or1(IExp1 a, IExp1 b) {
        this.a = a;
        this.b = b;
    }

    public IExp1 getA() {
        return a;
    }

    public IExp1 getB() {
        return b;
    }

    public IExp1 and(IExp1 exp) {
        return new And1(this, exp);
    }

    public IExp1 or(IExp1 exp) {
        return new Or1(this, exp);
    }

    public IExp1 xor(IExp1 exp) {
        return new Xor1(this, exp);
    }

    public IExp1 not() {
        return new Not1(this);
    }

    @Override
    public String toString() {
        return "(" + a + " & " + b + ")";
    }

}
