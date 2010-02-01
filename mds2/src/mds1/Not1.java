package mds1;

import java.util.HashMap;

public class Not1 implements IExp1 {

    private IExp1 e;

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
        IExp1 opt = context.get(this);
        if (opt == null) {
            e = e.optimize(context);
            if (e == Const1.create(true)) {
                opt = Const1.create(false);
            } else if (e == Const1.create(false)) {
                opt = Const1.create(true);
            } else {
                opt = this;
            }
            context.put(this, opt);
            return opt;
        } else {
            return opt;
        }
    }

}