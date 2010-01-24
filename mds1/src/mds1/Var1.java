package mds1;

import java.io.PrintStream;
import java.util.HashMap;

public class Var1 implements IExp1 {

    private final String name;
    private final boolean invert;
    private final Var1 not;

    public Var1(String name) {
        this.name = name;
        this.invert = false;
        this.not = new Var1(name, true, this);
    }

    public Var1(String name, boolean invert) {
        this.name = name;
        this.invert = invert;
        this.not = new Var1(name, !invert, this);
    }

    private Var1(String name, boolean invert, Var1 not) {
        this.name = name;
        this.invert = invert;
        this.not = not;
    }

    public String getName() {
        return name;
    }

    public boolean isInverted() {
        return invert;
    }

    public IExp1 and(IExp1 exp) {
        if (exp instanceof Const1) {
            return exp.and(this);
        }
        if (equals(exp)) {
            return this;
        }
        if (not().equals(exp)) {
            return Const1.create(false);
        }
        return new And1(this, exp);
    }

    public IExp1 or(IExp1 exp) {
        if (exp instanceof Const1) {
            return exp.or(this);
        }
        if (equals(exp)) {
            return this;
        }
        if (not().equals(exp)) {
            return Const1.create(true);
        }
        if (exp instanceof And1)
            return exp.or(this);
        return new Or1(this, exp);
    }

    public IExp1 xor(IExp1 exp) {
        if (exp instanceof Const1) {
            return exp.xor(this);
        }
        if (equals(exp)) {
            return Const1.create(false);
        }
        if (not().equals(exp)) {
            return Const1.create(true);
        }
        return exp.not().and(this).or(this.not().and(exp));
    }

    public Var1 not() {
        return not;
    }

    public boolean hasDisjunctions() {
        return false;
    }

    public Var1 getVar() {
        return this;
    }

    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        IExp1 sub = context.get(this);
        if (sub == null) {
            if (equals(v)) {
                sub = c;
            } else if (not().equals(v)) {
                sub = c.not();
            } else {
                sub = this;
            }
        }
        context.put(this, sub);
        return sub;
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
            Var1 v = (Var1) o;
            return name.equals(v.name) && (invert == v.invert);
        } else {
            return false;
        }
    }

    public void print(PrintStream out) {
        out.print(invert ? "!" : "");
        out.print(name);
    }

}
