package net.swined.prime.binary;

import java.math.BigInteger;

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
  
  private static IExpression[] xor(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] r = new IExpression[a.length];
    for (int i = 0; i < r.length; i++)
      r[i] = xor(a[i], b[i]);
    return r;
  }
  
  private static IExpression[] sum(IExpression[] a, IExpression[] b) {
    if (a.length != b.length)
      throw new IllegalArgumentException();
    IExpression[] q = new IExpression[a.length];
    q[0] = Const.ZERO;
    for (int i = 0; i < q.length - 1; i++)
        q[i + 1] = a[i].and(b[i]).or(q[i].and(a[i].or(b[i])));
    return xor(xor(a, b), q);
  }
  
  private static IExpression[] mul(IExpression[] a, IExpression[] b) {
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
  
  private static IExpression split(IExpression e, Var v) {
	  return v.and(e.sub(v, Const.ONE)).or(v.not().and(e.sub(v.not(), Const.ZERO)));
  }
  
  private static IExpression eq(BigInteger n) {
	int l = n.bitLength() / 2 + 1;
    Var[] a = var("a", l);
    Var[] b = var("b", l);
    IExpression e = eq(mul(a, b), n);
    for (Var x : a)
    	e = split(e, x);
    for (Var x : b)
    	e = split(e, x);
    return e;
  }
  
  public static void main(String[] args) {
    System.out.println("1 == " + eq(BigInteger.valueOf(4096)));
  }
 
}
