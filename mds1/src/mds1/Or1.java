package mds1;

import java.io.PrintStream;
import java.util.HashMap;

public class Or1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private final Var1 var;
    private IExp1 not = null;

    public Or1(IExp1 a, IExp1 b) {
        this.a = a;
        this.b = b;
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

    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        IExp1 sub = context.get(this);
        if (sub == null) {
            IExp1 sa = a.sub(context, v, c);
            IExp1 sb = b.sub(context, v, c);
            if (sa == a && sb == b)
                sub = this;
            else
                sub = sa.or(sb);
        }
        context.put(this, sub);
        return sub;
    }

    public boolean hasDisjunctions() {
        return true;
    }

    public Var1 getVar() {
        return var;
    }

    public void print(PrintStream out) {
        out.print("(");
        a.print(out);
        out.print(" | ");
        b.print(out);
        out.print(")");
    }

}
