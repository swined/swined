package org.swined.zpol.v1;

import java.util.Map;

public enum Const implements IB {

	ZERO, ONE;

	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		return this;
	}
	
}
