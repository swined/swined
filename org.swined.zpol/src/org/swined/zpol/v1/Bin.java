package org.swined.zpol.v1;

import java.util.WeakHashMap;

public class Bin {

	private Bin() {
		
	}
	
	public static IB sub(IB x, int v, boolean c) {
		return x.sub(v, c, new WeakHashMap<IB, IB>());
	}
	
}
