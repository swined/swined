package net.swined.prime.binary;

public class Xor extends Expression {

  private final IExpression a;
  private final IExpression b;

  public Xor(IExpression a, IExpression b) {
    if (a instanceof Const || b instanceof Const)
      throw new IllegalArgumentException();
    this.a = a;
    this.b = b;
    a.getVars(vars);
    b.getVars(vars);
  }

  @Override
  public IExpression not() {
    return new Not(this);
  }

  @Override
  public IExpression sub(Var v, Const c, SubContext ctx) {
    IExpression sub = ctx.xor.get(this);
    if (sub == null) {
      IExpression sa = a.sub(v, c, ctx);
      IExpression sb = b.sub(v, c, ctx);
      sub = sa.xor(sb);
      ctx.xor.put(this, sub);
    }
    return sub;
  }

  @Override
  public String toString() {
    return "(" + a + " ^ " + b + ")";
  }

  @Override
  public Var getVar() {
    Var va = a.getVar();
    if (va != null)
      return va;
    Var vb = b.getVar();
    if (vb != null)
      return vb;
    return null;
  }

}
