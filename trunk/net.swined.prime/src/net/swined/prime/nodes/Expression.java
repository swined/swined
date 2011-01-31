package net.swined.prime.nodes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public abstract class Expression implements IExpression {

    protected final BigInteger vars;

    public Expression(IExpression a) {
      this.vars = a.getVars();
      if (a instanceof Const) {
          throw new IllegalArgumentException();
      }
    }
    
    public Expression(IExpression a, IExpression b) {
      this.vars = a.getVars().or(b.getVars());
      if (a instanceof Const || b instanceof Const) {
          throw new IllegalArgumentException();
      }
    }

    public Expression(BigInteger vars) {
      this.vars = vars;
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
        if (sub == null)
            ctx.put(this, sub = subImpl(v, c, ctx));
        return sub;
    }

    @Override
    public final IExpression sub(int v, Const c) {
        return sub(v, c, new HashMap<IExpression, IExpression>());
    }

    protected abstract IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx);
}
