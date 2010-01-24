package mds1;

import java.io.PrintStream;

public class And1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private IExp1 sub = null;
    private Var1 subVar = null;
    private Const1 subConst = null;
    private IExp1 not = null;
    private final boolean hasDisjunctions;
    private final Var1 var;

    public And1(IExp1 a, IExp1 b) {
        this.a = a;
        this.b = b;
        hasDisjunctions = a.hasDisjunctions() || b.hasDisjunctions();
        Var1 tv = a.getVar();
        if (tv == null)
            var = b.getVar();
        else
            var = tv;
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
        return exp.not().and(this).or(not().and(exp));
    }

    public void setNot(IExp1 not) {
        if (this.not == null)
            this.not = not;
        else
            throw new UnsupportedOperationException();
    }

    public IExp1 not() {
        if (not == null) {
            not = a.not().or(b.not());
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
            IExp1 sa = a.sub(v, c);
            IExp1 sb = b.sub(v, c);
            if (sa == a && sb == b)
                sub = this;
            else
                sub = sa.and(sb);
        }
        return sub;
    }

    public boolean hasDisjunctions() {
        return hasDisjunctions;
    }

    public Var1 getVar() {
        return var;
    }

    public void print(PrintStream out) {
        out.print("(");
        a.print(out);
        out.print(" & ");
        b.print(out);
        out.print(")");
    }

}
