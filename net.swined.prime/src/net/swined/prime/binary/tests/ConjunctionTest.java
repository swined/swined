package net.swined.prime.binary.tests;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import net.swined.prime.binary.Conjunction;
import net.swined.prime.binary.Const;
import net.swined.prime.binary.Disjunction;
import net.swined.prime.binary.IExpression;
import net.swined.prime.binary.Or;

import org.junit.Test;

public class ConjunctionTest {

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor() {
    new Conjunction(BigInteger.ZERO, BigInteger.ZERO);
  }
  
  @Test
  public void testAnd0() {
    IExpression v1 = Conjunction.var(0, true);
    IExpression v2 = Conjunction.var(0, false);
    IExpression v3 = Disjunction.var(0, true);    
    Assert.assertEquals("0", v1.and(v2).toString());
    Assert.assertEquals("0", v1.and(Const.ZERO).toString());
    Assert.assertEquals("!x0", v1.and(Const.ONE).toString());
    Assert.assertEquals("!x0", v1.and(new Or(v2, v3)).toString());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testAnd1() {
    IExpression v = Conjunction.var(0, true);
    IExpression e = new IExpression() {
      
      @Override
      public IExpression sub(Integer v, Const c, Map<IExpression, IExpression> map) {
        return null;
      }
      
      @Override
      public IExpression or(IExpression e) {
        return null;
      }
      
      @Override
      public IExpression not() {
        return null;
      }
      
      @Override
      public void getVars(Set<Integer> vars) {
      }
      
      @Override
      public IExpression and(IExpression e) {
        return null;
      }
    };
    v.and(e);
  }
  
  @Test
  public void testOr() {
    IExpression v0 = Conjunction.var(0, true);
    IExpression v1 = new Conjunction(BigInteger.valueOf(3), BigInteger.ZERO);
    Assert.assertEquals("!x0", v0.or(Const.ZERO).toString());
    Assert.assertEquals("(!x0 | x0 & x1)", v0.or(v1).toString());
  }
  
  @Test
  public void testNot() {
    IExpression v1 = new Conjunction(BigInteger.valueOf(3), BigInteger.ZERO);
    Assert.assertEquals("(!x0 | !x1)", v1.not().toString());
  }
  
  @Test
  public void testGetVars() {
    IExpression v1 = Conjunction.var(0, true);
    Set<Integer> vars = new HashSet<Integer>();
    v1.getVars(vars);
    Assert.assertEquals("[0]", vars.toString());
  }

  @Test
  public void testSub() {
    IExpression e1 = new Conjunction(BigInteger.valueOf(3), BigInteger.ZERO);
    IExpression e2 = new Conjunction(BigInteger.valueOf(1), BigInteger.ZERO);
    Assert.assertEquals("x1", e1.sub(0, Const.ONE, null).toString());
    Assert.assertEquals("0", e1.sub(0, Const.ZERO, null).toString());
    Assert.assertEquals("x0 & x1", e1.sub(2, Const.ONE, null).toString());
    Assert.assertEquals("1", e2.sub(0, Const.ONE, null).toString());
  }
  
}
