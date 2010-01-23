package mds1;

import java.util.HashSet;

public class Const1 implements IExp1 {

    private final boolean value;
    private static final Const1 TRUE = new Const1(true);
    private static final Const1 FALSE = new Const1(false);

    private Const1(boolean value) {
        this.value = value;
    }

    public static Const1 create(boolean value) {
        return value ? TRUE : FALSE;
    }

    public boolean getValue() {
        return value;
    }

    public IExp1 and(IExp1 exp) {
        if (value) {
            return exp;
        } else {
            return this;
        }
    }

    public IExp1 or(IExp1 exp) {
        if (value) {
            return this;
        } else {
            return exp;
        }
    }
    
    public IExp1 xor(IExp1 exp) {
        if (value) {
            return exp.not();
        } else {
            return exp;
        }
    }

    public IExp1 not() {
        return Const1.create(!value);
    }

    public void getVars(HashSet<Var1> vars) {
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

}
