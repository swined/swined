package mds1;

public class And1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private IExp1 sub = null;
    private String string = null;
    private PDNF pdnf = null;
    private IExp1 not = null;

    public And1(IExp1 a, IExp1 b) {
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
        return exp.not().and(this).or(this.not().and(exp));
    }

    public void setNot(IExp1 not) {
        if (this.not == null)
            this.not = not;
        else
            throw new UnsupportedOperationException();
    }

    public IExp1 not() {
        if (not == null) {
            not = a.not().and(b.not());
            not.setNot(this);
        }
        return not;
    }

    public IExp1 substitute(Var1 v, Const1 c) {
        if (sub == null)
            sub = a.substitute(v, c).and(b.substitute(v, c));
        return sub;
    }

    public PDNF toPDNF() {
        if (pdnf == null)
            pdnf = a.toPDNF().and(b.toPDNF());
        return pdnf;
    }

    @Override
    public String toString() {
        if (string == null)
            string = "(" + a + " & " + b + ")";
        return string;
    }

}
