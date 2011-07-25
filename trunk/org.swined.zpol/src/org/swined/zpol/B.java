package org.swined.zpol;

import java.util.WeakHashMap;

public class B {

	public static IB sub(IB x, int v, C c) {
		return x.sub(v, c, new WeakHashMap<IB, IB>());
	}
	
	public static IB m2(IB a, IB b, IB c) {
		return X.get(A.get(a, b), A.get(X.get(a, b), c));
	}
	
}
