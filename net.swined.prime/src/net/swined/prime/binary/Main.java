package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static IExpression[] var(int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) {
            e[i] = new Var(i, false);
        }
        return e;
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

    private static BigInteger divisor(BigInteger n) {
    	int l = n.bitLength() / 2 + n.bitLength() % 2;
    	IExpression[] d = Int.toExp(n);
    	System.out.println("building (" + l + ")");
    	IExpression e = eq(Int.mod(d, var(l)), BigInteger.ZERO);
    	System.out.println(e.complexity());
    	//System.out.println(e);
    	System.out.println("solving");
    	Map<Integer, Const> solution = solve(e);
    	if (solution == null)
    		return null;
		return extract(0, l, solution);
//    	return BigInteger.ONE;
    }

    private static BigInteger key(int l) {
    	BigInteger n = BigInteger.ZERO.setBit(l / 2).nextProbablePrime();
    	return n.multiply(n.nextProbablePrime());
    }

    public static void main(String[] args) {
    	BigInteger n = key(10);
    	System.out.println(n);
    	System.out.println(toBinary(n));
		BigInteger d = divisor(n);
		System.out.println(d);
		if (!n.mod(d).equals(BigInteger.ZERO))
			System.err.println("wrong solution");
    }
}
