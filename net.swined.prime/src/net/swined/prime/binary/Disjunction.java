package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

public class Disjunction implements IExpression {

  private final BigInteger vars;
  private final BigInteger sign;
  
  public Disjunction(BigInteger vars, BigInteger sign) {
    if (vars.bitLength() == 0)
      throw new IllegalArgumentException();
    this.vars = vars;
    this.sign = sign;
  }

  public static Disjunction var(int var, boolean negative) {
    BigInteger zero = BigInteger.ZERO;
    BigInteger vars = zero.setBit(var);
    BigInteger sign = negative ? zero.setBit(var) : zero;
    return new Disjunction(vars, sign);
  }
  
  @Override
  public IExpression and(IExpression e) {
    if (e instanceof Const)
      return e.and(this);
    return new And(this, e);
  }

  @Override
  public IExpression or(IExpression e) {
    if (e instanceof Const)
      return e.or(this);
    if (e instanceof Disjunction) {
        Disjunction c = (Disjunction) e;
        if (vars.and(c.vars).and(sign.xor(c.sign)).equals(BigInteger.ZERO)) {
          return Const.ONE;
        } else {
          return new Disjunction(vars.or(c.vars), sign.or(c.sign));
        }
    }
    return new Or(this, e);
  }

  @Override
  public IExpression not() {
    return new Conjunction(vars, sign.not().and(vars));
  }

  @Override
  public IExpression sub(Integer v, Const c, Map<IExpression, IExpression> map) {
    if (vars.testBit(v)) {
      if (Const.get(sign.testBit(v)) == c)
        return Const.ZERO;
      else {
        BigInteger n = vars.clearBit(v);
        if (n.bitLength() == 0)
          return Const.ONE;
        else
          return new Disjunction(n, sign);
      }
    } else return this;
  }

  @Override
  public void getVars(Set<Integer> vars) {
    for (int i = 0; i < this.vars.bitLength(); i++)
      if (this.vars.testBit(i))
        vars.add(i);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < vars.bitLength(); i++)
      if (vars.testBit(i)) {
        if (sb.length() > 0)
          sb.append(" | ");
        if (sign.testBit(i))
          sb.append("!");
        sb.append("x");
        sb.append(i);
      }
    return sb.toString();
  }
  
}
