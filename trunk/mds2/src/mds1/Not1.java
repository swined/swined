package mds1;

import java.util.HashMap;

public class Not1 implements IExp1 {

    private final IExp1 e;

    public Not1(IExp1 e) {
        this.e = e;
    }

    public IExp1 getE() {
        return e;
    }

    @Override
    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        return this;
    }

    @Override
    public IExp1 optimize(HashMap<IExp1, IExp1> context) {
        return this;
    }

}