package mds1;

import java.io.PrintStream;
import java.util.HashMap;

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

    public void setNot(IExp1 not) {
    }

    public Const1 not() {
        return Const1.create(!value);
    }

    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        return this;
    }

    public boolean hasDisjunctions() {
        return false;
    }

    public Var1 getVar() {
        return null;
    }

    public void print(PrintStream out) {
        out.print(value ? "1" : "0");
    }

}
