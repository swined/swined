package net.swined.prime;

import java.math.BigInteger;
import java.util.WeakHashMap;

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
    	  IExpression x = e.sub(v, Const.ONE, new WeakHashMap<IExpression, IExpression>());
    	  System.out.print(",");
    	  IExpression y = e.sub(v, Const.ZERO, new WeakHashMap<IExpression, IExpression>());
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
      BigInteger na = Int.toInt(a);
      BigInteger nb = Int.toInt(b);
      BigInteger nn = na.multiply(nb);
      System.out.println(na + " * " + nb + " = " + nn + " (" + n + ")"); 
    }
    
    private static final BigInteger RSA100 = new BigInteger("1522605027922533360535618378132637429718068114961380688657908494580122963258952897654000350692006139");
    
    public static void main(String[] args) {
      try {
        div(RSA100);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  
} 
