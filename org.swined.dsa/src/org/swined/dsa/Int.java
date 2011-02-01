package org.swined.dsa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.swined.dsa.integer.BitMul;
import org.swined.dsa.integer.Bits;
import org.swined.dsa.integer.IInteger;
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

    public static IExpression[] mulMod(IExpression[] a, IExpression[] b, BigInteger m) {
        IExpression[] r = zero(a.length + b.length);
        for (int i = 0; i < a.length; i++) {
        	IExpression[] t = zero(r.length);
        	for (int j = 0; j < b.length; j++)
        		t[i + j] = Bin.and(a[i], b[j]);
        	r = sum(r, t);
        	r = split(r, getVars(r)); // FIXME explosive!
        }
    	return r;
    }
    
    // cmb(A, b) = mb(A, b) | [!b]
    // mb(c1, f1) * mb(c2, f2) = mb(c1 * c2, f1 & f2)
    // *{cmb(C_i, x_i)} = *{mb(C_i >> 1, x_i) << 1 + (C_i & 1) ^ [x_i]}
    // (mb(C_i >> 1, x_i) << 1 + (C_i & 1) ^ [x_i]) * (mb(C_j >> 1, x_j) << 1 + (C_j & 1) ^ [x_j]) = 
    // 
    
    public static IExpression[] mulMod(IExpression[][] x, BigInteger m) {
    	List<IExpression[]> y = new ArrayList<IExpression[]>();
    	for (IExpression[] e : x)
    		y.add(e);
    	while (true) {
    		IExpression[] p = y.remove(0);
    		if (y.size() == 0)
       			return p;
    		IExpression[] q = y.remove(0);
    		System.out.println("vars");
    		BigInteger vars = getVars(p).or(getVars(q));
    		if (vars.bitCount() > 2) {
    			y.add(p);
    			y.add(q);
    			break;
    		}
    		System.out.println("mul");
    		IExpression[] z = mulMod(p, q, m);
    		System.out.println("split");
    		z = split(z, vars);
    		System.out.println("mod");
    		z = mod(z, m);
    		System.out.println("split");
    		z = split(z, vars);
    		System.out.println("done");
			y.add(z);
			break;
    	}
    	for (IExpression[] z : y)
    		System.out.println(Arrays.toString(z));
    	throw new UnsupportedOperationException();
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
    
    public static IInteger modPow(BigInteger a, IExpression[] x, BigInteger m) {
    	IInteger r = new org.swined.dsa.integer.Const(BigInteger.ONE);
    	if (BigInteger.ZERO.equals(a))
    		return r; 
    	for (int i = 0; i < x.length; i++) {
    		IExpression[] t = new IExpression[a.bitLength()];
        if (t.length > 0)
//            t[0] = a.testBit(0) ? Const.ONE : Bin.not(x[i]);
//    		for (int j = 1; j < t.length; j++) 
//    			t[j] = a.testBit(j) ? x[i] : Const.ZERO;
//    		t = mod(t, m);
    		r = new Mul(r, new Sum(new BitMul(new org.swined.dsa.integer.Const(a.clearBit(0)), x[i]), new Bits(new IExpression[] { a.testBit(0) ? Const.ONE : Bin.not(x[i]) })));
    		a = a.multiply(a).mod(m);
    	}
    	return new Mod(r, m);
    }
    
}
