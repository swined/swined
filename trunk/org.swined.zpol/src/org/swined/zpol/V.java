package org.swined.zpol;

import java.util.Map;

public class V implements IB {

	private final int n;
	
	private V(int n) {
		this.n = n;
	}
	
	public static V get(int n) {
		return new V(n);
	}

	@Override
	public boolean isFree(int n, Map<IB, Boolean> ctx) {
		return this.n != n;
	}

	@Override
	public IB sub(int v, C c, Map<IB, IB> ctx) {
		return v == n ? c : this;
	}

	@Override
	public int getVar() {
		return n;
	}
	
}
