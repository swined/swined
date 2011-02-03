package org.swined.dsa;

import java.math.BigInteger;
import java.util.Arrays;

import org.swined.dsa.integer.Bit;
import org.swined.dsa.integer.IInteger;
import org.swined.dsa.integer.IntConst;
import org.swined.dsa.integer.Mod;
import org.swined.dsa.integer.Mul;
import org.swined.dsa.integer.Sum;

public class Int {

	public static BigInteger getVars(IExpression[] e) {
		BigInteger r = BigInteger.ZERO;
		for (IExpression x : e)
			r = r.or(x.getVars());
		return r;
	}

	public static IExpression[] split(IExpression[] e, BigInteger vars) {
		IExpression[] r = new IExpression[e.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = Bin.split(e[i], vars);
		}
		return r;
	}
	
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

    public static IExpression[] negate(IExpression[] a) {
    	IExpression[] r = new IExpression[a.length];
    	for (int i = 0; i < a.length; i++)
    		r[i] = Bin.not(a[i]);
    	return sum(r, Int.pad(new IExpression[] { Const.ONE }, r.length));
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

    public static IExpression[] mulMod(IExpression[] a, BigInteger b, BigInteger m) {
        IExpression[] r = zero(a.length + b.bitLength());
        for (int i = 0; i < a.length; i++) {
        	IExpression[] t = zero(r.length);
        	for (int j = 0; j < b.bitLength(); j++)
        		t[i + j] = b.testBit(i) ? a[j] : Const.ZERO;
        	t = mod(t, m);
        	r = sum(r, t);
        	r = mod(r, m);
        }
    	return r;
    }
    
    public static BigInteger mpc(BigInteger a, BigInteger m, int ix) {
    	for (int i = 0; i < ix; i++)
    		a = a.multiply(a).mod(m);
    	return a;
    }
    
    public static IInteger modPow(BigInteger a, IExpression[] x, BigInteger m) {
    	IInteger r = new IntConst(BigInteger.ONE);
    	if (BigInteger.ZERO.equals(a))
    		return r; 
    	for (int i = 0; i < x.length; i++) {
    		BigInteger c = mpc(a, m, i);
    		Mul p = new Mul(new IntConst(c.clearBit(0)), new Bit(x[i]));
			Bit q = new Bit(c.testBit(0) ? Const.ONE : Bin.not(x[i]));
			r = new Mul(r, new Sum(p, q));
    	}
    	return new Mod(r, m);
    }
    
}
