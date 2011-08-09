package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Int {

	private Int() {
	}

	private static IB get(IB[] a, int i) {
		if (a == null)
			return null;
		if (i >= 0 && i < a.length)
			return a[i];
		else
			return Const.ZERO;
	}

	public static IB[] vars(int s, int c) {
		IB[] z = new IB[c];
		for (int i = 0; i < c; i++)
			z[i] = Var.get(i + s);
		return z;
	}

	public static IB[] zero() {
		IB[] z = new IB[1];
		z[0] = Const.ZERO;
		return z;
	}
	
	public static IB[] sum(IB[] a, IB[] b) {
		List<IB> r = new ArrayList<IB>();
		IB f = Const.ZERO;
	    int i = 0;
	    while (i <= a.length || i <= b.length) {
	    	r.add(Xor.get(f, Xor.get(get(a, i), get(b, i))));
	    	f = Bin.m2(get(a, i), get(b, i), f);
	    	i++;
	    }
		return r.toArray(new IB[0]);
	}

    public static IB[] mul(IB[] a, IB[] b) {
    	IB[] r = zero();
        for (int i = 0; i < a.length; i++) {
        	IB[] t = new IB[i + b.length];
        	for (int j = 0; j < t.length; j++)
        		t[j] = Const.ZERO;
        	for (int j = 0; j < b.length; j++)
        		t[i + j] = And.get(a[i], b[j]);
        	r = sum(r, t);
        }
    	return r;
    }
	
    public static IB eq(IB[] z, BigInteger n) {
    	IB r = Const.ONE;
    	for (int i = 0; i < z.length; i++)
    		r = And.get(r, Xor.get(z[i], n.testBit(i) ? Const.ZERO : Const.ONE));
    	return r;
    }

}
