package net.swined.prime;

import java.util.Map;

public class Or implements IExpression {

  private final IExpression a;
  private final IExpression b;

  public Or(IExpression a, IExpression b) {
    if (a instanceof Const || b instanceof Const)
      throw new IllegalArgumentException();
    if (a instanceof Var && b instanceof Var) {
      Var va = (Var)a;
      Var vb = (Var)b;
      if (va.name == vb.name)
        throw new IllegalArgumentException();
    }
    this.a = a;
    this.b = b;
  }
  
  @Override
  public String toString() {
    return "(" + a + " | " + b + ")";
  }
  
  @Override
  public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
    IExpression s = ctx.get(this);
    if (s == null) {
      IExpression sa = a.sub(v, c, ctx);
      IExpression sb = b.sub(v, c, ctx);
      if (sa != a || sb != b)
        s = Bin.or(sa, sb);
      else
        s = this;
      ctx.put(this, s);
    }
    return s;
  }

  @Override
  public int getVar() {
    return a.getVar();
  }

  @Override
  public boolean hasVar(int v) {
    return a.hasVar(v) || b.hasVar(v);
  }

}
