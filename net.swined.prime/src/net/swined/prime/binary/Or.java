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
        this.a = a;
        this.b = b;
    }

    @Override
    public IExpression not() {
        return new Not(this);
    }

    @Override
    public String toString() {
        return "(" + a + " | " + b + ")";
    }

    @Override
    protected IExpression subImpl(Var v, Const c, Map<IExpression, IExpression> ctx) {
      IExpression sa = a.sub(v, c, ctx);
      IExpression sb = b.sub(v, c, ctx);
      return sa.or(sb);
    }

}
