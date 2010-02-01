package mds1;

import java.util.HashMap;

public class Or1 implements IExp1 {

    private final IExp1 a;
    private final IExp1 b;

    public Or1(IExp1 a, IExp1 b) {
        this.a = a;
        this.b = b;
    }

    public IExp1 getA() {
        return a;
    }

    public IExp1 getB() {
        return b;
    }

    @Override
    public IExp1 sub(HashMap<IExp1, IExp1> context, Var1 v, Const1 c) {
        IExp1 sub = context.get(this);
        if (sub == null) {
            sub = new Or1(a.sub(context, v, c), b.sub(context, v, c));
            context.put(this, sub);
        }
        return sub;
    }

    @Override
    public IExp1 optimize(HashMap<IExp1, IExp1> context) {
        IExp1 opt = context.get(this);
        if (opt == null) {
            IExp1 oa = a.optimize(context);
            IExp1 ob = b.optimize(context);
            if (oa == Const1.create(true)) {
                opt = Const1.create(true);
            } else if (oa == Const1.create(false)) {
                opt = ob;
            } else if (ob == Const1.create(true)) {
                opt = Const1.create(true);
            } else if (ob == Const1.create(false)) {
                opt = oa;
            } else {
                opt = new Or1(oa, ob);
            }
            context.put(this, opt);
        }
        return opt;
    }

}
