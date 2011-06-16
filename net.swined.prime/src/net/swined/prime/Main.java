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

    private static int findNonConst(IExpression[] t) {
      for (int i = 0; i < t.length; i++) {
        //t[i] = Bin.split(t[i]);
        if (!(t[i] instanceof Const))
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
    
    private static Var v(int n) {
      return new Var(n, false);
    }

    private static Var V(int n) {
      return new Var(n, true);
    }
    
    private static void div(BigInteger n) {
      final IExpression[] a = var(0, n.bitLength() / 2);
      final IExpression[] b = var(a.length, n.bitLength() - a.length);
      sub(0, Const.ONE, a, b);
      sub(5, Const.ONE, a, b);
      sub(6, V(1), a, b);
      sub(7, V(2), a, b);
      sub(8, Bin.xor(V(1), v(2), v(3)), a, b);
      
      final IExpression[] t = Int.mul(a, b);
      // 12349

      // 11011
      // 11110
      // 00001
      // 10001
      // 11101
      // 11000
      // 01001
      // 01110
      // 10010
      // 10110
      // 00111
      // 00010
      // }, {1=0, 2=1, 3=0, 4=1, 9=0}, {1=0, 2=0, 3=1, 4=0, 9=0}, {1=0, 2=1, 3=1, 4=0, 9=1}, {1=1, 2=0, 3=1, 4=0, 9=1}]
    

      
      for (int i = 0; i < 5; i++)
        System.out.println(Bin.solve(t[i]));
    }
    
    public static void main(String[] args) {
      try {
        div(key(10));
      } catch (Exception e) {
        System.out.println("fail");
      }
    }
    
} 
