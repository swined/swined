package org.swined.zpol.v1;

import java.util.Map;

public class Var implements IB {

	public final int v;
	
	private Var(int v) {
		this.v = v;
	}

	public static IB get(int v) {
		return new Var(v);
	}
	
	@Override
	public IB sub(int v, Const c, Map<IB, IB> ctx) {
		return this.v == v ? c : this;
	}
	
	@Override
	public String toString() {
		return String.format("x%s", v);
	}
	
}
