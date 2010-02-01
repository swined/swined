package mds1;

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

    @Override
    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        return this;
    }

    @Override
    public IExp1 optimize(HashMap<IExp1, IExp1> context) {
        return this;
    }

    @Override
    public String toString() {
        return value ? "1" : "0";
    }

}
