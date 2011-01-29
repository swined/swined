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
    	return a[l].and(b[l].not()).or(xor(a[l], b[l].not()).and(g));
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

    public static IExpression[] shl(IExpression[] a, int l) {
    	IExpression[] r = zero(a.length);
    	for (int i = l; i < a.length; i++)
    		r[i] = a[i - l];
    	return r;
    }
    
    public static IExpression[] negate(IExpression[] a) {
    	IExpression[] r = new IExpression[a.length];
    	for (int i = 0; i < a.length; i++)
    		r[i] = a[i].not();
    	return sum(r, Int.pad(new IExpression[] { Const.ONE }, r.length));
    }
    
    public static IExpression[] mod(IExpression[] a, IExpression[] b) {
    	IExpression[] r = Arrays.copyOf(a, a.length);
    	for (int i = a.length - b.length; i >= 0; i--) {
    		IExpression[] p = shl(Int.pad(b, r.length), i);
			IExpression ge = Int.ge(r, p);
			for (int j = 0; j < p.length; j++)
				p[j] = ge.and(p[j]);
			r = sum(r, negate(p));
    	}
    	return r;
    }

    private static IExpression xor(IExpression a, IExpression b) {
    	return (a.and(b.not()).or(a.not().and(b)));
    }
    
    public static IExpression[] sum(IExpression[] a, IExpression[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException();
        }
        IExpression[] q = new IExpression[a.length];
        IExpression f = Const.ZERO;
        for (int i = 0; i < q.length; i++) {
            q[i] = xor(f, xor(a[i], b[i]));
            f = f.and(a[i]).or(f.and(b[i])).or(a[i].and(b[i]));
        }
        return q;
    }

    public static IExpression[] zero(int l) {
        IExpression[] e = new IExpression[l];
        for (int i = 0; i < l; i++) {
            e[i] = Const.ZERO;
        }
        return e;
    }
    
}
