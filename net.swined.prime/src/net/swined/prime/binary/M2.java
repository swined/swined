package net.swined.prime.binary;

import java.util.Map;


public class M2 extends Expression {

    private final IExpression a;
    private final IExpression b;
    private final IExpression c;

    public M2(IExpression a, IExpression b, IExpression c) {
        super(a.getVars().or(b.getVars()).or(c.getVars()));
    if (a instanceof Const || b instanceof Const || c instanceof Const)
      throw new IllegalArgumentException();
        this.a = a;
        this.b = b;
        this.c = c;
    }

  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  protected IExpression subImpl(Var v, Const c, Map<IExpression, IExpression> ctx) {
    IExpression sa = a.sub(v, c, ctx);
    IExpression sb = b.sub(v, c, ctx);
    IExpression sc = this.c.sub(v, c, ctx);
    return sa.m2(sb, sc);
  }

  @Override
  public String toString() {
    return "2(" + a + ", " + b + ", " + c + ")";
  }

}
