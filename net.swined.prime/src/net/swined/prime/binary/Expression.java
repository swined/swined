package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public abstract class Expression implements IExpression {

    protected final BigInteger vars;

    public Expression(BigInteger vars) {
        this.vars = vars;
    }

    @Override
    public final IExpression and(IExpression e) {
        if (e instanceof Const) {
            return e.and(this);
        }
        return new And(this, e);
    }

    @Override
    public final IExpression or(IExpression e) {
        if (e instanceof Const) {
            return e.or(this);
        }
        return new Or(this, e);
    }

    @Override
    public final IExpression xor(IExpression e) {
        if (e instanceof Const) {
            return e.xor(this);
        }
        return new Xor(this, e);
    }

    @Override
    public final IExpression m2(IExpression x, IExpression y) {
        if (x instanceof Const) {
            return x.m2(this, y);
        }
        if (y instanceof Const) {
            return y.m2(this, x);
        }
        return new M2(this, x, y);
    }

    @Override
    public final BigInteger getVars() {
        return vars;
    }

    @Override
    public final IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (!vars.testBit(v)) {
            return this;
        }
        IExpression sub = ctx.get(this);
        if (sub == null) {
            ctx.put(this, sub = subImpl(v, c, ctx));
        }
        return sub;
    }

    @Override
    public final IExpression sub(int v, Const c) {
        return sub(v, c, new HashMap<IExpression, IExpression>());
    }

    protected abstract IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx);
}
