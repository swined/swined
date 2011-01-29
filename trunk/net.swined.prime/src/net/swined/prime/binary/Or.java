package net.swined.prime.binary;

import java.util.Map;

public class Or extends Expression {

    private final IExpression a;
    private final IExpression b;

    public Or(IExpression a, IExpression b) {
        super(a.getVars().or(b.getVars()));
        if (a instanceof Const || b instanceof Const) {
            throw new IllegalArgumentException();
        }
        if (a instanceof Var && b instanceof Var) {
            throw new IllegalArgumentException();
        }
        if (a instanceof Disjunction && b instanceof Disjunction) {
            throw new IllegalArgumentException();
        }
        this.a = a;
        this.b = b;
    }

    @Override
    protected IExpression notImpl() {
        return a.not().and(b.not());
    }

    @Override
    public String toString() {
        return "(" + a + " | " + b + ")";
    }

    @Override
    protected IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx) {
        IExpression sa = a.sub(v, c, ctx);
        IExpression sb = b.sub(v, c, ctx);
        return sa.or(sb);
    }
}
