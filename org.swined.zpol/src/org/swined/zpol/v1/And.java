package org.swined.zpol.v1;

public class And implements IB {

	public final IB a;
	public final IB b;
	
	private And(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		return new And(a, b);
	}
}
