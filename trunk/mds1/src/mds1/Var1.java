package mds1;

import java.io.PrintStream;
import java.math.BigInteger;
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

    @Override
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
        if (exp.depth() < 100) {
            HashMap<IExp1, BigInteger> dc = new HashMap();
            if (exp.depends(dc, this).compareTo(BigInteger.ZERO) > 0) {
                System.out.println("cleaning and");
                BigInteger c0 = Main.complexity(exp);
                HashMap<IExp1, IExp1> sc = new HashMap();
                IExp1 n = exp.sub(sc, this, Const1.create(true));
                BigInteger c1 = Main.complexity(n);
                System.out.println(c0.subtract(c1));
                return this.and(n);
            }
        }
        return new And1(this, exp);
    }

    @Override
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
        if (exp.depth() < 100) {
            HashMap<IExp1, BigInteger> dc = new HashMap();
            if (exp.depends(dc, this).compareTo(BigInteger.ZERO) > 0) {
                System.out.println("cleaning or");
                BigInteger c0 = Main.complexity(exp);
                HashMap<IExp1, IExp1> sc = new HashMap();
                IExp1 n = exp.sub(sc, this, Const1.create(false));
                BigInteger c1 = Main.complexity(n);
                System.out.println(c0.subtract(c1));
                return this.or(n);
            }
        }
        return new Or1(this, exp);
    }

    @Override
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

    @Override
    public Var1 not() {
        return not;
    }

    @Override
    public boolean hasDisjunctions() {
        return false;
    }

    @Override
    public Var1 getVar() {
        return this;
    }

    @Override
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
            context.put(this, sub);
        }
        return sub;
    }

    @Override
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

    @Override
    public BigInteger depends(HashMap<IExp1, BigInteger> context, Var1 v) {
        BigInteger dep = context.get(this);
        if (dep == null) {
            if (name.equals(v.getName())) {
                dep = BigInteger.valueOf(1);
            } else {
                dep = BigInteger.valueOf(0);
            }
            context.put(this, dep);
        }
        return dep;
    }

    @Override
    public void print(PrintStream out) {
        out.print(invert ? "!" : "");
        out.print(name);
    }

    @Override
    public int depth() {
        return 0;
    }

}
