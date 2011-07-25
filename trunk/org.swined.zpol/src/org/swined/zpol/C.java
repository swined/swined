package org.swined.zpol;

import java.util.Map;

public enum C implements IB {

	ZERO,
	ONE;

	@Override
	public boolean isFree(int n) {
		return true;
	}

	@Override
	public IB sub(int v, C c, Map<IB, IB> ctx) {
		return this;
	}
	
}
