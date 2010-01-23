package mds1;

import java.util.HashSet;

public class And1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;
    private HashSet<Var1> vars = null;

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

    public void getVars(HashSet<Var1> vars) {
        if (this.vars == null) {
            this.vars = new HashSet();
            a.getVars(this.vars);
            b.getVars(this.vars);
        }
        vars.addAll(this.vars);
    }

    @Override
    public String toString() {
        return "(" + a + " & " + b + ")";
    }

}
