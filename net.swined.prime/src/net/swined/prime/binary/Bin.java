package net.swined.prime.binary;

import java.math.BigInteger;

public class Bin {

	public static IExpression split(IExpression e) {
		BigInteger vars = e.getVars();
		for (int i = 0; i < vars.bitLength(); i++)
			if (vars.testBit(i)) {
				IExpression p = BinOps.and(new Var(i), e.sub(i, Const.ONE));
				IExpression n = BinOps.and(BinOps.not(new Var(i)), e.sub(i, Const.ZERO));
				e = BinOps.or(p, n);
			}
		return e;
	}
	
}
