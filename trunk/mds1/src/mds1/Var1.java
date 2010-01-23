package mds1;

public class Var1 implements IExp1 {

    private final String name;
    private final boolean invert;
    private final PDNF pdnf;

    public Var1(String name) {
        this.name = name;
        this.invert = false;
        this.pdnf = new PDNF(this);
    }

    public Var1(String name, boolean invert) {
        this.name = name;
        this.invert = invert;
        this.pdnf = new PDNF(this);
    }

    public String getName() {
        return name;
    }

    public boolean isInverted() {
        return invert;
    }

    public IExp1 and(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.and(this);
        if (equals(exp))
            return this;
        return new And1(this, exp);
    }

    public IExp1 or(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.or(this);
        if (equals(exp))
            return this;
        return new Or1(this, exp);
    }

    public IExp1 xor(IExp1 exp) {
        return exp.not().and(this).or(this.not().and(exp));
    }

    public IExp1 not() {
        return new Var1(name, !invert);
    }

    public IExp1 substitute(Var1 v, Const1 c) {
        if (this.equals(v)) {
            return c;
        } else {
            return this;
        }
    }

    public IExp1 optimize() {
        return this;
    }

    public void setNot(IExp1 not) {
    }

    @Override
    public String toString() {
        return (invert ? "!" : "") + name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Var1) {
            return name.equals(((Var1)o).name) && (invert == ((Var1)o).invert);
        } else {
            return false;
        }
    }

    public PDNF toPDNF() {
        return pdnf;
    }

}
