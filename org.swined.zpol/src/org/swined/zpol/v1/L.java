package org.swined.zpol.v1;

public class L implements IB {

	public final int v;
	public final IB a;
	public final IB b;
	
	private L(int v, IB a, IB b) {
		this.v = v;
		this.a = a;
		this.b = b;
	}
	
	public static IB get(int v, IB a, IB b) {
		return new L(v, a, b);
	}
	
}
