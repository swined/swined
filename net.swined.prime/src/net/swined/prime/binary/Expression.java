package net.swined.prime.binary;

public abstract class Expression implements IExpression {

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

}
