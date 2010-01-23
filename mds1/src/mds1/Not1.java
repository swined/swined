package mds1;

public class Not1 implements IExp1 {

    private final IExp1 exp;

    public Not1(IExp1 exp) {
        this.exp = exp;
    }

    public IExp1 getExp() {
        return exp;
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
        return exp;
    }

    @Override
    public String toString() {
        return "!" + exp;
    }

}
