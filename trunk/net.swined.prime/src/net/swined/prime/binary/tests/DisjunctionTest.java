package net.swined.prime.binary.tests;

import junit.framework.Assert;
import net.swined.prime.binary.Disjunction;
import net.swined.prime.binary.IExpression;

import org.junit.Test;


public class DisjunctionTest {

  @Test
  public void testVar() {
    Assert.assertEquals("x0", Disjunction.var(0, false).toString());
    Assert.assertEquals("!x0", Disjunction.var(0, true).toString());
    Assert.assertEquals("x42", Disjunction.var(42, false).toString());
    Assert.assertEquals("!x42", Disjunction.var(42, true).toString());
  }
  
  @Test
  public void testAnd() {
    IExpression v1 = Disjunction.var(0, true);
    IExpression v2 = Disjunction.var(0, false);
    Assert.assertEquals("0", v1.and(v2).toString());
  }

  @Test
  public void testOr() {
    IExpression v1 = Disjunction.var(0, true);
    IExpression v2 = Disjunction.var(0, false);
    Assert.assertEquals("1", v1.or(v2).toString());
  }
  
//  @Override
//  public IExpression not() {
//    return new Conjunction(vars, sign.not().and(vars));
//  }
//
//  @Override
//  public IExpression sub(Integer v, Const c, Map<IExpression, IExpression> map) {
//    if (vars.testBit(v)) {
//      if (Const.get(sign.testBit(v)) != c)
//        return Const.ONE;
//      else {
//        BigInteger n = vars.clearBit(v);
//        if (n.bitLength() == 0)
//          return Const.ZERO;
//        else
//          return new Disjunction(n, sign);
//      }
//    } else return this;
//  }
//
//  @Override
//  public void getVars(Set<Integer> vars) {
//    for (int i = 0; i < this.vars.bitLength(); i++)
//      if (this.vars.testBit(i))
//        vars.add(i);
//  }
//  
//  @Override
//  public String toString() {
//    StringBuilder sb = new StringBuilder();
//    for (int i = 0; i < vars.bitLength(); i++)
//      if (vars.testBit(i)) {
//        if (sb.length() > 0)
//          sb.append(" | ");
//        if (sign.testBit(i))
//          sb.append("!");
//        sb.append("x");
//        sb.append(i);
//      }
//    return sb.toString();
//  }
  
}
