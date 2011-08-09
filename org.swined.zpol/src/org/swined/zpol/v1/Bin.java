package org.swined.zpol.v1;

import java.util.WeakHashMap;

public class Bin {

	private Bin() {
		
	}
	
	public static IB sub(IB x, int v, boolean c) {
		return x.sub(v, c, new WeakHashMap<IB, IB>());
	}
	
	public static IB m2(IB a, IB b, IB c) {
		return Xor.get(And.get(c, Xor.get(a, b)), And.get(a, b));
	}
}
