package mds1;

import java.util.HashMap;

public class Var1 implements IExp1 {

    private final String name;

    public Var1(String name) {
        this.name = name;
    }

    @Override
    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        return this;
    }

    @Override
    public IExp1 optimize(HashMap<IExp1, IExp1> context) {
        return this;
    }


    public String getName() {
        return name;
    }

}
