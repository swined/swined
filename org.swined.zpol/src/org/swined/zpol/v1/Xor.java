package org.swined.zpol.v1;

public class Xor implements IB {

	public final IB a;
	public final IB b;
	
	private Xor(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		return new Xor(a, b);
	}
	
}
