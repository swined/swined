package net.swined.prime.binary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Expression implements IExpression {

  protected final Set<Var> vars = new HashSet<Var>();
  
  @Override
  public final IExpression and(IExpression e) {
    if (e instanceof Const)
      return e.and(this);
    return new And(this, e);
  }

  @Override
  public final IExpression or(IExpression e) {
    if (e instanceof Const)
      return e.or(this);
    return new Or(this, e);
  }

  @Override
  public final IExpression xor(IExpression e) {
    if (e instanceof Const)
      return e.xor(this);
    return new Xor(this, e);
  }

  @Override
  public final IExpression m2(IExpression x, IExpression y) {
    if (x instanceof Const)
      return x.m2(this, y);
    if (y instanceof Const)
      return y.m2(this, x);
    return new M2(this, x, y);
  }

  @Override
  public final void getVars(Set<Var> vars) {
    vars.addAll(this.vars);
  }
  
  @Override
  public final Var getVar() {
    if (vars.isEmpty())
      return null;
    else
      return vars.iterator().next();
  }
  
  @Override
  public final IExpression sub(Var v, Const c, Map<IExpression, IExpression> ctx) {
    if (!vars.contains(v))
      return this;
    IExpression sub = ctx.get(this);
    if (sub == null)
      ctx.put(this, sub = subImpl(v, c, ctx)); 
    return sub;
  }

  @Override
  public final IExpression sub(Var v, Const c) {
    return sub(v, c, new HashMap<IExpression, IExpression>());
  }
  
  protected abstract IExpression subImpl(Var v, Const c, Map<IExpression, IExpression> ctx);
  
}
