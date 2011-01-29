package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.Arrays;

public class Int {

	public static IExpression[] toExp(BigInteger x) {
		Const[] c = new Const[x.bitLength()];
		for (int i = 0; i < c.length; i++)
			c[i] = x.testBit(i) ? Const.ONE : Const.ZERO;
		return c;
	}
	
	public static BigInteger toInt(IExpression[] c) {
		BigInteger r = BigInteger.ZERO;
		for (int i = 0; i < c.length; i++) {
			if (c[i] instanceof Const) {
				if (c[i] == Const.ONE)
					r = r.setBit(i);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return r;
	}
	
    public static IExpression ge(IExpression[] a, IExpression[] b) {
    	if (a.length != b.length)
    		throw new IllegalArgumentException();
    	int l = a.length - 1;
    	IExpression g = Const.ONE;
    	if (l > 0)
    		g = ge(Arrays.copyOf(a, l), Arrays.copyOf(b, l)); 
    	return a[l].and(b[l].not()).or(a[l].xor(b[l].not()).and(g));
    }

    public static IExpression[] pad(IExpression[] a, int l) {
    	if (a.length == l)
    		return a;
    	IExpression[] r = Arrays.copyOf(a, l);
    	if (l > a.length)
    		for (int i = a.length; i < l; i++)
    			r[i] = Const.ZERO;
   		return r;
    }
    
}
