package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;

public class Main {

  private static Var[] var(String n, int l) {
	Var[] e = new Var[l];
    for (int i = 0; i < l; i++)
      e[i] = new Var(n + i, false);
    return e;
  }

  private static IExpression[] zero(int l) {
    IExpression[] e = new IExpression[l];
    for (int i = 0; i < l; i++)
      e[i] = Const.ZERO;
    return e;
  }
  
  private static IExpression xor(IExpression a, IExpression b) {
    return a.and(b.not()).or(a.not().and(b));
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] q = new IExpression[a.length];
    IExpression f = Const.ZERO;
    for (int i = 0; i < q.length; i++) {
      q[i] = xor(xor(a[i], b[i]), f);
      f = a[i].and(b[i]).or(a[i].and(f)).or(b[i].and(f));
    }
    return q;
  }
  
  private static IExpression[] mul(Var[] a, Var[] b) {
    IExpression[] r = zero(a.length + b.length);
    for (int i = 0; i < a.length; i++) {
      IExpression[] t = zero(r.length);
      for (int j = 0; j < b.length; j++)
        t[i + j] = a[i].and(b[j]);
      r = sum(r, t);
    }
    return r;
  }
  
  private static IExpression eq(IExpression[] e, BigInteger n) {
	  IExpression r = Const.ZERO;
	  for (int i = 0; i <  e.length; i++) {
		  IExpression b = n.testBit(i) ? Const.ONE : Const.ZERO;
		  r = r.or(xor(e[i], b));
	  }
	  return r.not();
  }
  
  private static IExpression split(IExpression e, Var[] v) {
	  for (Var x : v) {
      IExpression px = e.sub(x, Const.ONE, new HashMap<IExpression, IExpression>());
      IExpression nx = e.sub(x.not(), Const.ZERO, new HashMap<IExpression, IExpression>());
      e = x.and(px).or(x.not().and(nx));
    }
	  return e;
  }

  private static IExpression eq(BigInteger n) {
    int l = n.bitLength() / 2 + n.bitLength() % 2;
    Var[] a = var("a", l);
    Var[] b = var("b", l);
    IExpression[] m = mul(a, b);
  	IExpression e = eq(m, n);
    e = split(e, a);
    e = split(e, b);
    return e;
  }
  
  public static void main(String[] args) {
    BigInteger n = new BigInteger("9173");//9173503");
    System.out.println(n.bitLength() + " bit");
    IExpression e = eq(n);
    System.out.println("1 == " + e);
  }

}
