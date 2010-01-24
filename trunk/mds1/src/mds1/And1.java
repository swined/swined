package mds1;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.HashMap;

public class And1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private final boolean hasDisjunctions;
    private final Var1 var;
    private final int depth;
    private IExp1 not = null;

    public And1(IExp1 a, IExp1 b) {
        this.a = a;
        this.b = b;
        hasDisjunctions = a.hasDisjunctions() || b.hasDisjunctions();
        Var1 tv = a.getVar();
        if (tv == null)
            var = b.getVar();
        else
            var = tv;
        int da = a.depth();
        int db = b.depth();
        depth = (da > db ? da : db) + 1;
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
        if (a.equals(exp))
            return a;
        if (b.equals(exp))
            return b;
        if (exp instanceof And1) {
            And1 and = (And1)exp;
            if (a.equals(and.a))
                return a.and(b.or(and.b));
            if (a.equals(and.b))
                return a.and(b.or(and.a));
            if (b.equals(and.a))
                return b.and(a.or(and.b));
            if (b.equals(and.b))
                return b.and(a.or(and.a));
        }
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

    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        IExp1 sub = context.get(this);
        if (sub == null) {
            IExp1 sa = a.sub(context, v, c);
            IExp1 sb = b.sub(context, v, c);
            if (sa == a && sb == b)
                sub = this;
            else
                sub = sa.and(sb);
            context.put(this, sub);
        }
        return sub;
    }

    public BigInteger depends(HashMap<IExp1, BigInteger> context, Var1 v) {
        BigInteger dep = context.get(this);
        if (dep == null) {
            dep = a.depends(context, v).add(b.depends(context, v));
            context.put(this, dep);
        }
        return dep;
    }

    public boolean hasDisjunctions() {
        return hasDisjunctions;
    }

    public Var1 getVar() {
        return var;
    }

    public int depth() {
        return depth;
    }

    public void print(PrintStream out) {
        out.print("(");
        a.print(out);
        out.print(" & ");
        b.print(out);
        out.print(")");
    }

}