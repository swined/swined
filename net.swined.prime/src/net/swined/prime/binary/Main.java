package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

    private static BigInteger counter = BigInteger.ZERO;

    private static Var[] var(int n, int l) {
        Var[] e = new Var[l];
        for (int i = 0; i < l; i++) {
            e[i] = new Var(n + i);
        }
        return e;
    }

    private static IExpression[] zero(int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) {
            e[i] = Const.ZERO;
        }
        return e;
    }

    private static IExpression[] sum(IExpression[] a, IExpression[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException();
        }
        IExpression[] q = new IExpression[a.length];
        IExpression f = Const.ZERO;
        for (int i = 0; i < q.length; i++) {
            q[i] = f.xor(a[i].xor(b[i]));
            f = f.m2(a[i], b[i]);
        }
        return q;
    }

    private static IExpression[] mul(IExpression[] a, IExpression[] b) {
        IExpression[] r = zero(a.length + b.length);
        for (int i = 0; i < a.length; i++) {
            IExpression[] t = zero(r.length);
            for (int j = 0; j < b.length; j++) {
                t[i + j] = a[i].and(b[j]);
            }
            r = sum(r, t);
        }
        return r;
    }

    private static BigInteger extract(int n, int l, Map<Var, Const> s) {
        BigInteger r = BigInteger.ZERO;
        for (Var v : s.keySet()) {
            if (v.name >= n && v.name < n + l) {
                if (s.get(v) == Const.ONE) {
                    r = r.setBit(v.name - n);
                }
            }
        }
        return r;
    }

    private static IExpression eq(IExpression[] e, BigInteger n) {
        IExpression r = Const.ONE;
        for (int i = 0; i < e.length; i++) {
            IExpression x = n.testBit(i) ? e[i] : e[i].not();
            r = r.and(x);
        }
        return r;
    }

    private static IExpression eq(BigInteger n) {
        int l = n.bitLength() / 2 + n.bitLength() % 2;
        return eq(mul(var(0, l), var(n.bitLength(), l)), n);
    }

    private static String toBinary(BigInteger n) {
        StringBuilder sb = new StringBuilder();
        for (int i = n.bitLength(); i > 0; i--) {
            sb.append(n.testBit(i - 1) ? "1" : "0");
        }
        sb.append("b/");
        sb.append(n.bitLength());
        return sb.toString();
    }

    private static Map<Var, Const> solve(IExpression eq) {
        counter = counter.add(BigInteger.ONE);
        if (eq == Const.ONE) {
            return new HashMap<Var, Const>();
        }
        if (eq == Const.ZERO) {
            return null;
        }
        Var var = eq.getVar();
        for (Const c : Const.values()) {
            Map<Var, Const> s = solve(eq.sub(var, c));
            if (s != null) {
                s.put(var, c);
                return s;
            }
        }
        return null;
    }

    private static IExpression split(IExpression e) {
      Set<Var> vars = new HashSet<Var>();
      e.getVars(vars);
      for (Var var : vars) {
        IExpression p = e.sub(var, Const.ONE);
        IExpression n = e.sub(var, Const.ZERO);
        e = var.and(p).or(var.not().and(n));
      }
      return e;
    }
    
    private static BigInteger eu(BigInteger n) {
      System.out.println("building eq");
        IExpression eq = eq(n);
//        System.out.println(eq);
        System.out.println("splitting");
        eq = split(eq);
//        System.out.println(eq);
        System.out.println("solving");
        Map<Var, Const> solution = solve(eq);
        System.out.println("analyzing solution");
        if (solution == null) {
            return null;
        } else {
            BigInteger x = extract(0, n.bitLength(), solution);
            BigInteger y = extract(n.bitLength(), n.bitLength(), solution);
            if (!x.multiply(y).equals(n))
                throw new AssertionError();
            System.out.println(x + " * " + y + " = " + n);
            return x.subtract(BigInteger.ONE).multiply(y.subtract(BigInteger.ONE));
        }
    }

    public static void main(String[] args) {
        BigInteger n = new BigInteger("1000").nextProbablePrime();//9173503");
        n = n.multiply(n.nextProbablePrime());
        System.out.println(toBinary(n));
        System.out.println(eu(n));
    }
}
