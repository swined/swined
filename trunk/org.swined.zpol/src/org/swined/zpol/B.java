package org.swined.zpol;

public class B {

	public static IB m2(IB a, IB b, IB c) {
		return X.get(A.get(a, b), A.get(X.get(a, b), c));
	}
	
}
