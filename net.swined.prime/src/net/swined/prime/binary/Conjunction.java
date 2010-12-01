package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class Conjunction implements IExpression {

  private final BigInteger vars;
  private final BigInteger sign;
  
  public Conjunction(BigInteger vars, BigInteger sign) {
    this.vars = vars;
    this.sign = sign;
  }

  @Override
  public IExpression and(IExpression e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IExpression or(IExpression e) {
    throw new UnsupportedOperationException();
  }

  @Override
  public IExpression not() {
    throw new UnsupportedOperationException();
  }

  @Override
  public IExpression sub(Var v, Const c, Map<IExpression, IExpression> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void getVars(Set<Var> vars) {
    throw new UnsupportedOperationException();
  }
  
}
