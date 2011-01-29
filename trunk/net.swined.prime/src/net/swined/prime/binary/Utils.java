package net.swined.prime.binary;

import java.math.BigInteger;

public class Utils {

	public static Const[] intToExp(BigInteger x) {
		Const[] c = new Const[x.bitLength()];
		for (int i = 0; i < c.length; i++)
			c[i] = x.testBit(i) ? Const.ONE : Const.ZERO;
		return c;
	}
	
	public static BigInteger expToInt(Const[] c) {
		BigInteger r = BigInteger.ZERO;
		for (int i = 0; i < c.length; i++)
			if (c[i] == Const.ONE)
				r = r.setBit(i);
		return r;
	}
	
}
