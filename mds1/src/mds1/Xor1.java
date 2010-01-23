package mds1;

public class Xor1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private IExp1 sub = null;
    private String string = null;

    private Xor1(IExp1 a, IExp1 b) {
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
        if (exp instanceof Const1)
            return exp.and(this);
        return new And1(this, exp);
    }

    public IExp1 or(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.or(this);
        return new Or1(this, exp);
    }

    public IExp1 xor(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.xor(this);
        return new Xor1(this, exp);
    }

    public IExp1 not() {
        return new Not1(this);
    }

    public IExp1 substitute(Var1 v, Const1 c) {
        if (sub == null)
            sub = a.substitute(v, c).xor(b.substitute(v, c));
        return sub;
    }

    public IExp1 optimize() {
        return a.xor(b);
    }

    public PDNF toPDNF() {
        return a.toPDNF().xor(b.toPDNF()).toPDNF();
    }

    @Override
    public String toString() {
        if (Main.timeout())
            return "xor";
        if (string == null)
            string = "(" + a + " ^ " + b + ")";
        return string;
    }

}
