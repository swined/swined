package mds1;

public class Or1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private IExp1 sub = null;
    private Var1 subVar;
    private Const1 subConst;
    private String string = null;
    private PDNF pdnf = null;
    private IExp1 not = null;

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
        if (exp instanceof Const1)
            return exp.and(this);
        if (exp instanceof Var1)
            return a.and(exp).or(b.and(exp));
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

    public IExp1 sub(Var1 v, Const1 c) {
        if (sub != null)
            if (!v.equals(subVar))
                sub = null;
        if (sub != null)
            if (!c.equals(subConst))
                sub = null;
        if (sub == null) {
            subVar = v;
            subConst = c;
            sub = a.sub(v, c).or(b.sub(v, c));
        }
        return sub;
    }

    public PDNF toPDNF() {
        if (pdnf == null)
            pdnf = a.toPDNF().or(b.toPDNF());
        return pdnf;
    }

    @Override
    public String toString() {
        if (string == null)
            string = "(" + a + " | " + b + ")";
        return string;
    }

}
