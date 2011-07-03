package net.swined.prime;

import java.math.BigInteger;

public class Main {

    private static IExpression[] var(int o, int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) 
            e[i] = new Var(i + o);
        return e;
    }

    private static int getVar(IExpression[]... x) {
      for (IExpression[] y : x)
        for (IExpression z : y) {
          int v = z.getVar();
          if (v >= 0)
            return v;
        }
      return -1;
    }
        
    private static void sub(int v, IExpression e, IExpression[]... x) {
      for (IExpression[] y : x) 
        for (int i = 0; i < y.length; i++)
          y[i] = Bin.sub(y[i], v, e);
    }
    
    private static IExpression eq(IExpression[] x, BigInteger n) {
    	if (n.bitLength() > x.length)
    		throw new IllegalArgumentException();
    	IExpression r = Const.ONE;
    	for (int i = 0; i < x.length; i++)
    		r = Bin.and(r, n.testBit(i) ? x[i] : Bin.not(x[i]));
    	return r;
    }
    
    private static void div(BigInteger n) {
      final IExpression[] a = var(0, n.bitLength() / 2 + 1);
      final IExpression[] b = var(a.length, a.length);
      IExpression e = eq(Int.mul(a, b), n);
      while (true) {
    	  System.out.print(".");
    	  int v = e.getVar();
    	  if (v < 0)
    		  break;
    	  IExpression x = Bin.sub(e, v, Const.ONE);
    	  System.out.print(",");
    	  IExpression y = Bin.sub(e, v, Const.ZERO);
    	  sub(v, x, a, b);
    	  e = Bin.or(x, y);
      }
      System.out.println();
      while (true) {
    	  int v = getVar(a, b);
    	  if (v < 0)
    		  break;
    	  sub(v, Const.ONE, a, b);
      }
      if (e != Const.ONE)
    	  throw new RuntimeException(e.toString());
      BigInteger na = Int.toInt(a);
      BigInteger nb = Int.toInt(b);
      BigInteger nn = na.multiply(nb);
      System.out.println(na + " * " + nb + " = " + nn);
      if (!nn.equals(n))
    	  throw new RuntimeException("!= " + n);
    }

    private static void sat(BigInteger n) {
        final IExpression[] a = var(0, n.bitLength() / 2 + 1);
        final IExpression[] b = var(a.length, a.length);
        IExpression e = eq(Int.mul(a, b), n);
        while (true) {
      	  System.out.print(".");
      	  int v = e.getVar();
      	  if (v < 0)
      		  break;
      	  e = Bin.exclude(e, v);
        }
        System.out.println();
        System.out.println(e);
      }
    
    private static final BigInteger WTF = new BigInteger("121");
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
    private static final BigInteger RSA120 = new BigInteger("227010481295437363334259960947493668895875336466084780038173258247009162675779735389791151574049166747880487470296548479");
    
    public static void main(String[] args) {
      try {
        div(RSA100);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  
} 
