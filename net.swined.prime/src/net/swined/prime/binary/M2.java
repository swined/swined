package net.swined.prime.binary;


public class M2 extends Expression {

    private final IExpression a;
    private final IExpression b;
    private final IExpression c;

    public M2(IExpression a, IExpression b, IExpression c) {
    if (a instanceof Const || b instanceof Const || c instanceof Const)
      throw new IllegalArgumentException();
        this.a = a;
        this.b = b;
        this.c = c;
        a.getVars(vars);
        b.getVars(vars);
        c.getVars(vars);
    }

  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  public IExpression sub(Var v, Const c, SubContext ctx) {
    IExpression sub = ctx.m2.get(this);
    if (sub == null) {
      IExpression sa = a.sub(v, c, ctx);
      IExpression sb = b.sub(v, c, ctx);
      IExpression sc = this.c.sub(v, c, ctx);
      sub = sa.m2(sb, sc);
      ctx.m2.put(this, sub);
    }
    return sub;
  }

  @Override
  public String toString() {
    return "2(" + a + ", " + b + ", " + c + ")";
  }

  @Override
  public Var getVar() {
    Var va = a.getVar();
    if (va != null)
      return va;
    Var vb = b.getVar();
    if (vb != null)
      return vb;
    Var vc = c.getVar();
    if (vc != null)
      return vc;
    return null;
  }

}
