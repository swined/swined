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
      s = Bin.or(a.sub(v, c, ctx), b.sub(v, c, ctx));
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

  @Override
  public IExpression eo(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    IExpression s = co.get(this);
    if (s == null) {
      s = Bin.or(a.eo(v, co, ca), b.eo(v, co, ca));
      co.put(this, s);
    }
    return s;
  }

  @Override
  public IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    // (f0 | g0) & (f1 | g1)
    // f0 f1 | g0 g1 | f0 g1 | g0 f1
    throw new RuntimeException();
  }
  
}
