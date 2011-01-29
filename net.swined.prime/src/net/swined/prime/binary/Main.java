package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static IExpression[] var(int n, int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) {
            e[i] = Conjunction.var(n + i, false);
        }
        return e;
    }

    private static IExpression[] mul(IExpression[] a, IExpression[] b) {
        IExpression[] r = Int.zero(a.length + b.length);
        for (int i = 0; i < a.length; i++) {
            IExpression[] t = Int.zero(r.length);
            for (int j = 0; j < b.length; j++) {
                t[i + j] = a[i].and(b[j]);
            }
            r = Int.sum(r, t);
          //  System.out.println(Arrays.toString(r));
        }
        return r;
    }

    private static BigInteger extract(int n, int l, Map<Integer, Const> s) {
        BigInteger r = BigInteger.ZERO;
        for (int v : s.keySet()) {
            if (v >= n && v < n + l) {
                if (s.get(v) == Const.ONE) {
                    r = r.setBit(v - n);
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

    private static Map<Integer, Const> solve(IExpression eq) {
        if (eq == Const.ONE) {
            return new HashMap<Integer, Const>();
        }
        if (eq == Const.ZERO) {
            return null;
        }
        int var = eq.getVars().getLowestSetBit();
        for (Const c : Const.values()) {
            Map<Integer, Const> s = solve(eq.sub(var, c));
            if (s != null) {
                s.put(var, c);
                return s;
            }
        }
        return null;
    }

    private static IExpression split(IExpression e) {
        BigInteger vars = e.getVars();
        for (int i = 0; i < vars.bitLength(); i++) {
            if (vars.testBit(i)) {
                Conjunction var = Conjunction.var(i, false);
                IExpression p = e.sub(i, Const.ONE);
                IExpression n = e.sub(i, Const.ZERO);
                e = var.and(p).or(var.not().and(n));
            }
        }
        return e;
    }

    private static BigInteger eu(BigInteger n) {
        System.out.println("building");
        IExpression eq = eq(n);
        System.out.println(eq);
        System.out.println("splitting");
        eq = split(eq);
//        System.out.println(eq);
        System.out.println("solving");
        Map<Integer, Const> solution = solve(eq);
        System.out.println("analyzing");
        if (solution == null) {
            return null;
        } else {
            BigInteger x = extract(0, n.bitLength(), solution);
            BigInteger y = extract(n.bitLength(), n.bitLength(), solution);
            if (!x.multiply(y).equals(n)) {
                throw new AssertionError();
            }
            System.out.println(x + " * " + y + " = " + n);
            return x.subtract(BigInteger.ONE).multiply(y.subtract(BigInteger.ONE));
        }
    }

    private static BigInteger divisor(BigInteger n) {
    	int l = n.bitLength() / 2;
    	IExpression[] d = Int.toExp(n);
    	IExpression e = eq(Int.mod(d, var(0, l)), BigInteger.ZERO);
    	Map<Integer, Const> solution = solve(e);
    	if (solution == null)
    		return null;
		return extract(0, l, solution);
    }

    private static BigInteger key(int l) {
    	BigInteger n = BigInteger.ZERO.setBit(l / 2).nextProbablePrime();
    	return n.multiply(n.nextProbablePrime());
    }
    
    public static void main(String[] args) {
    	BigInteger n = key(70);
    	System.out.println(n);
    	System.out.println(toBinary(n));
		System.out.println(divisor(n));
    }
}
