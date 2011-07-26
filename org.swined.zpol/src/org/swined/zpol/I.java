package org.swined.zpol;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class I {

	private I() {
	}

	private static Z get(Z[] a, int i) {
		if (a == null)
			return null;
		if (i >= 0 && i < a.length)
			return a[i];
		else
			return Z.c(false);
	}

	public static Z[] vars(int s, int c) {
		Z[] z = new Z[c];
		for (int i = 0; i < c; i++)
			z[i] = Z.v(i + s);
		return z;
	}

	public static Z[] zero() {
		Z[] z = new Z[1];
		z[0] = Z.c(false);
		return z;
	}
	
	public static Z[] sum(Z[] a, Z[] b) {
		List<Z> r = new ArrayList<Z>();
	    Z f = Z.c(false);
	    int i = 0;
	    while (i <= a.length || i <= b.length) {
	    	r.add(f.xor(get(a, i), get(b, i)));
	    	f = f.m2(get(a, i), get(b, i));
	    	i++;
	    }
		return r.toArray(new Z[0]);
	}

    public static Z[] mul(Z[] a, Z[] b) {
        Z[] r = zero();
        for (int i = 0; i < a.length; i++) {
        	Z[] t = new Z[i + b.length];
        	for (int j = 0; j < t.length; j++)
        		t[j] = Z.c(false);
        	for (int j = 0; j < b.length; j++)
        		t[i + j] = a[i].and(b[j]);
        	r = sum(r, t);
        }
    	return r;
    }
	
    public static Z eq(Z[] z, BigInteger n) {
    	Z r = Z.c(true);
    	for (int i = 0; i < z.length; i++)
    		r = r.and(z[i].xor(Z.c(!n.testBit(i))));
    	return r;
    }

}
