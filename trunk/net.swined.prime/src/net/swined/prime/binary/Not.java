package net.swined.prime.binary;

import java.util.Map;

public class Not extends Expression {

    private final IExpression x;

    public Not(IExpression x) {
        super(x.getVars());
        if (x instanceof Const || x instanceof Not || x instanceof Xor || x instanceof Conjunction || x instanceof Disjunction) {
            throw new IllegalArgumentException();
        }
        this.x = x;
    }

    @Override
    public IExpression not() {
        return x;
    }

    @Override
    protected IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx) {
        IExpression sx = x.sub(v, c, ctx);
        return sx.not();
    }

    @Override
    public String toString() {
        return "!" + x;
    }
}
