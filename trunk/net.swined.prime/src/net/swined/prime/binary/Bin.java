package net.swined.prime.binary;

import java.math.BigInteger;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				IExpression p = new Var(i, false).and(e.sub(i, Const.ONE));
				IExpression n = new Var(i, true).and(e.sub(i, Const.ZERO));
				e = p.or(n);
			}
		return e;
	}
	
}
