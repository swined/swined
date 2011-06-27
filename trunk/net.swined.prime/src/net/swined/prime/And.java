package net.swined.prime;

import java.util.Map;

public class And implements IExpression {

  private final IExpression a;
  private final IExpression b;

  public And(IExpression a, IExpression b) {
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
    return "(" + a + " & " + b + ")";
  }
  
  @Override
  public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
    IExpression s = ctx.get(this);
    if (s == null) {
      s = Bin.and(a.sub(v, c, ctx), b.sub(v, c, ctx));
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
    
    // f0 & g0 | f1 & g1 = 
    // (f0 | f1) & g0 & g1 | f0 & g0 & !g1 | f1 & !g0 & g1 =
    
    //         eo fa ga fo go
    // 0000 00 0  0  0  0  0
    // 0001 00 0  0  0  0  1
    // 0010 00 0  0  0  0  1
    // 0100 00 0  0  0  1  0
    // 1000 00 0  0  0  1  0
    // 0101 01 1  0  0  1  1
    // 0110 00 0  0  0  1  1
    // 1001 00 0  0  0  1  1
    // 1010 10 1  0  0  1  1
    // 0011 00 0  0  1  0  1
    // 1011 10 1  0  1  1  1
    // 0111 01 1  0  1  1  1
    // 1100 00 0  1  0  1  0
    // 1101 01 1  1  0  1  1
    // 1110 10 1  1  0  1  1
    // 1111 11 1  1  1  1  1
    
    throw new RuntimeException();
  }

  @Override
  public IExpression ea(int v, Map<IExpression, IExpression> co, Map<IExpression, IExpression> ca) {
    IExpression s = ca.get(this);
    if (s == null) {
      s = Bin.and(a.ea(v, co, ca), b.ea(v, co, ca));
      ca.put(this, s);
    }
    return s;
  }

}
