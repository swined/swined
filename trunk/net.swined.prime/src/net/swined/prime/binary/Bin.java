package net.swined.prime.binary;

import java.math.BigInteger;

public class Bin {

	public static IExpression split(IExpression e) {
        long vc = e.getVars().bitCount();
        if (e.complexity().compareTo(BigInteger.valueOf(vc * (1 << vc))) < 0)
        	return e;
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++) {
			if (vars.testBit(i)) {
				IExpression p = new Var(i, false).and(e.sub(i, Const.ONE));
				IExpression n = new Var(i, true).and(e.sub(i, Const.ZERO));
				e = p.or(n);
			}
		}
		return e;
	}
	
}
