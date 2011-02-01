package org.swined.dsa;

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

    private static BigInteger extract(int l, Map<Integer, Const> s) {
    	if (s == null)
    		return null;
        BigInteger r = BigInteger.ZERO;
        for (int v : s.keySet()) {
            if (v < l) {
                if (s.get(v) == Const.ONE) {
                    r = r.setBit(v);
                }
            }
        }
        return r;
    }

    private static IExpression eq(IExpression[] e, BigInteger n) {
        IExpression r = Const.ONE;
        for (int i = 0; i < e.length; i++) {
            IExpression x = n.testBit(i) ? e[i] : Bin.not(e[i]);
            r = Bin.and(r, x);
        }
        return r;
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
            Map<Integer, Const> s = solve(eq.sub(var, c, new HashMap<IExpression, IExpression>()));
            if (s != null) {
                s.put(var, c);
                return s;
            }
        }
        return null;
    }

    public static void main(String[] args) {
    	IExpression[] x = Int.modPow(BigInteger.valueOf(5), var(5), BigInteger.valueOf(400));
//    	for (IExpression y : x)
//    		System.out.println(y);
    	IExpression eq = eq(x, BigInteger.valueOf(10));
    	//eq = Bin.split(eq);
    	System.out.println(extract(15, solve(eq)));
    }
}
