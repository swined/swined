package org.swined.zpol.v1;


public class Bin {

	private Bin() {
		
	}
	
	public static IB m2(IB a, IB b, IB c) {
		return Xor.get(And.get(c, Xor.get(a, b)), And.get(a, b));
	}
}
