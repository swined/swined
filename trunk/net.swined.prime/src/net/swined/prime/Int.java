package net.swined.prime;

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
    	IExpression g = Const.ONE;
    	for (int i = 0; i < a.length; i++)
    		g = Bin.ge(a[i], b[i], g);
    	return g;
    }

    public static IExpression ge(IExpression[] a, BigInteger b) {
    	if (a.length < b.bitLength())
    		return Const.ZERO;
    	IExpression g = Const.ONE;
    	for (int i = 0; i < a.length; i++) {
    		if (b.testBit(i)) {
    		    g = Bin.and(a[i], g);
    		} else {
    			g = Bin.or(a[i], g);    			
    		}
    	}
    	return g;
    }
    
    public static IExpression[] pad(IExpression[] a, int l) {
    	if (a.length == l)
    		return a;
    	IExpression[] r = new IExpression[l];
		for (int i = 0; i < l; i++)
			r[i] = i < a.length ? a[i] : Const.ZERO;
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
  			for (int j = 0; j < p.length; j++) {
  				p[j] = Bin.and(ge, p[j]);
  			}
  			r = sum(r, negate(p));
    	}
    	return r;
    }

    public static IExpression[] mod(IExpression[] a, BigInteger m) {
    	IExpression[] r = Arrays.copyOf(a, a.length);
    	for (int i = a.length - m.bitLength(); i >= 0; i--) {
    		BigInteger s = m.shiftLeft(i);
    		IExpression ge = Int.ge(r, s);
    		IExpression[] p = new IExpression[r.length];//(IExpression[])pad(toExp(s), r.length);
  			for (int j = 0; j < p.length; j++)
  				p[j] = Bin.and(ge, s.testBit(j) ? Const.ONE : Const.ZERO);
  			r = sum(r, negate(p));
    	}
    	return r;
    }
    
    public static IExpression[] sum(IExpression[] a, IExpression[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException();
        }
        IExpression[] q = new IExpression[a.length];
        IExpression f = Const.ZERO;
        for (int i = 0; i < q.length; i++) {
            q[i] = Bin.xor(a[i], b[i], f);
            f = Bin.m2(a[i], b[i], f);
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

    public static IExpression[] mul(IExpression[] a, IExpression[] b) {
        IExpression[] r = zero(a.length + b.length);
        for (int i = 0; i < a.length; i++) {
        	IExpression[] t = zero(r.length);
        	for (int j = 0; j < b.length; j++)
        		t[i + j] = Bin.and(a[i], b[j]);
        	r = sum(r, t);
        }
    	return r;
    }
    
    public static IExpression[] pow(BigInteger a, IExpression[] b) {
    	if (BigInteger.ZERO.equals(a))
    		return new IExpression[] { Const.ZERO };
    	IExpression[] r = new IExpression[] { Const.ONE };
    	for (int i = 0; i < b.length; i++) {
    		IExpression[] t = new IExpression[a.bitLength()];
    		for (int j = 0; j < t.length; j++) 
    			t[j] = a.testBit(j) ? b[i] : Const.ZERO;
    		t[0] = Bin.or(t[0], b[i].not());
    		r = mul(r, t);
    		a = a.multiply(a);
    	}
    	return r;
    }

    public static IExpression[] modPow(BigInteger a, IExpression[] x, BigInteger m) {
    	if (BigInteger.ZERO.equals(a))
    		return new IExpression[] { Const.ZERO };
    	IExpression[] r = new IExpression[] { Const.ONE };
    	for (int i = 0; i < x.length; i++) {
    		IExpression[] t = new IExpression[a.bitLength()];
    		for (int j = 0; j < t.length; j++) 
    			t[j] = a.testBit(j) ? x[i] : Const.ZERO;
    		if (t.length > 0)
    			t[0] = Bin.or(t[0], x[i].not());
    		r = mul(r, mod(t, m));
    		a = a.multiply(a).mod(m);
    	}
    	return mod(r, m);
    }
    
}
