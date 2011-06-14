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
    	assert c.equals(d);
      return d;
    }

    private static int findNonConst(IExpression[] t) {
      for (int i = 0; i < t.length; i++) {
        t[i] = Bin.split(t[i]);
        if (t[i].getVars().bitCount() > 0)
          return i;
      }
      return -1;
    }
    
    private static int getVar(IExpression[]... x) {
      for (IExpression[] y : x)
        for (IExpression z : y) {
          int v = z.getVars().getLowestSetBit();
          if (v >= 0)
            return v;
        }
      return -1;
    }
    
    private static void sub(int v, IExpression e, IExpression[]... x) {
      for (IExpression[] y : x) 
        for (int i = 0; i < y.length; i++)
          y[i] = y[i].sub(v, e, new HashMap<IExpression, IExpression>());
    }
    
    private static void div(BigInteger n) {
      final int l = n.bitLength() / 2 + n.bitLength() % 2;
      final IExpression[] a = var(0, l);
      final IExpression[] b = var(l, l);
      while (true) {
        final IExpression[] t = Int.mul(a, b);
        int ix = findNonConst(t);
        if (ix < 0) 
          break;
        IExpression e = n.testBit(ix) ? t[ix] : t[ix].not();
        int v = e.getVars().getLowestSetBit();
        System.out.println(e);
        IExpression x = e.sub(v, Const.ONE, new HashMap<IExpression, IExpression>());
        IExpression y = e.sub(v, Const.ZERO, new HashMap<IExpression, IExpression>());
        System.out.println(x);
        System.out.println(y);
        IExpression s = Bin.and(x, y.not());
        System.out.println("x" + v + " = " + s);
        sub(v, s, a, b);
      }
      System.out.println(Arrays.toString(a));
      System.out.println(Arrays.toString(b));
      System.out.println(Arrays.toString(Int.mul(a, b)));
      System.out.println(Arrays.toString(Int.toExp(n)));
      while (true) {
        int v = getVar(a, b);
        if (v < 0)
          break;
        sub(v, Const.ZERO, a, b);
      }
      System.out.println(Arrays.toString(a));
      System.out.println(Arrays.toString(b));
      BigInteger u = Int.toInt(a);
      BigInteger v = Int.toInt(b);
      System.out.println(u);
      System.out.println(v);
      System.out.println(u.multiply(v));
      System.out.println(n);
    }
    
    public static void main(String[] args) {
      div(key(20));
    }
    
}
