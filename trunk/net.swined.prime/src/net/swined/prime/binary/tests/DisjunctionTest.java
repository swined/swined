package net.swined.prime.binary.tests;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import net.swined.prime.binary.Conjunction;
import net.swined.prime.binary.Const;
import net.swined.prime.binary.Disjunction;
import net.swined.prime.binary.IExpression;
import net.swined.prime.binary.Or;

import org.junit.Test;


public class DisjunctionTest {

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor() {
    new Disjunction(BigInteger.ZERO, BigInteger.ZERO);
  }
  
  @Test
  public void testVar() {
    Assert.assertEquals("(x0)", Disjunction.var(0, false).toString());
    Assert.assertEquals("(!x0)", Disjunction.var(0, true).toString());
    Assert.assertEquals("(x42)", Disjunction.var(42, false).toString());
    Assert.assertEquals("(!x42)", Disjunction.var(42, true).toString());
  }
  
  @Test
  public void testAnd() {
    IExpression v1 = Disjunction.var(0, true);
    IExpression v2 = Disjunction.var(0, false);
    IExpression v3 = Conjunction.var(0, true);    
    Assert.assertEquals("0", v1.and(v2).toString());
    Assert.assertEquals("0", v1.and(Const.ZERO).toString());
    Assert.assertEquals("(!x0)", v1.and(Const.ONE).toString());
    Assert.assertEquals("!x0", v1.and(new Or(v2, v3)).toString());
  }

  @Test
  public void testOr() {
    IExpression v1 = Disjunction.var(0, true);
    IExpression v2 = Disjunction.var(0, false);
    IExpression v3 = Disjunction.var(1, false);
    IExpression v4 = Conjunction.var(1, false);
    Assert.assertEquals("1", v1.or(Const.ONE).toString());
    Assert.assertEquals("1", v1.or(v2).toString());
    Assert.assertEquals("(x0 | x1)", v2.or(v3).toString());
    Assert.assertEquals("((x1) | x1)", v3.or(v4).toString());
  }
  
  @Test
  public void testNot() {
    Assert.assertEquals("!x0", Disjunction.var(0, false).not().toString());
  }
  
  @Test
  public void testGetVars() {
    IExpression v1 = Disjunction.var(0, true);
    Set<Integer> vars = new HashSet<Integer>();
    v1.getVars(vars);
    Assert.assertEquals("[0]", vars.toString());
  }
  
  @Test
  public void testSub() {
    IExpression e1 = new Disjunction(BigInteger.valueOf(3), BigInteger.ZERO);
    IExpression e2 = new Disjunction(BigInteger.valueOf(1), BigInteger.ZERO);
    Assert.assertEquals("1", e1.sub(0, Const.ONE, null).toString());
    Assert.assertEquals("(x1)", e1.sub(0, Const.ZERO, null).toString());
    Assert.assertEquals("(x0 | x1)", e1.sub(2, Const.ONE, null).toString());
    Assert.assertEquals("0", e2.sub(0, Const.ZERO, null).toString());
  }
  
}
