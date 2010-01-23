package mds1;

public class Var1 implements IExp1 {

    private final String name;

    public Var1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Var1) {
            return name.equals(((Var1)o).name);
        } else {
            return false;
        }
    }

}
