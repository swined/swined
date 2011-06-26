package net.swined.prime;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    private static IExpression[] var(int o, int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) 
            e[i] = new Var(i + o, false);
        return e;
    }

    private static BigInteger key(int l) {
    	BigInteger a = BigInteger.ZERO.setBit(l / 2).nextProbablePrime();
    	BigInteger b = a.nextProbablePrime();
    	BigInteger c = Int.toInt(Int.mul(Int.toExp(a), Int.toExp(b)));
    	BigInteger d = a.multiply(b);
    	if (!c.equals(d))
    	  throw new RuntimeException();
      return d;
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
      final IExpression[] a = var(0, n.bitLength() - 1);
      final IExpression[] b = var(a.length, n.bitLength() - 1);
      IExpression e = eq(Int.mul(a, b), n);
      while (true) {
    	  System.out.print(".");
    	  int v = e.getVar();
    	  if (v < 0)
    		  break;
    	  IExpression x = e.sub(v, Const.ONE, new HashMap<IExpression, IExpression>());
    	  IExpression y = e.sub(v, Const.ZERO, new HashMap<IExpression, IExpression>());
    	  sub(v, x, a, b);
    	  e = Bin.or(x, Bin.and(Bin.not(x), y));
    	  
      }
      System.out.println();
      System.out.println(Arrays.toString(a));
      System.out.println(Arrays.toString(b));
      System.out.println(e);
      while (true) {
    	  int v = getVar(a, b);
    	  if (v < 0)
    		  break;
    	  sub(v, Const.ONE, a, b);
      }
      BigInteger na = Int.toInt(a);
      BigInteger nb = Int.toInt(b);
      BigInteger nn = na.multiply(nb);
      System.out.println(na + " * " + nb + " = " + nn + " ~ " + n);
    }
    
    public static void main(String[] args) {
      try {
        div(key(12));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
} 
