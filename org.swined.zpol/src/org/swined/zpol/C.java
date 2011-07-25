package org.swined.zpol;

import java.math.BigInteger;
import java.util.Map;

public enum C implements IB {

	ZERO,
	ONE;

	@Override
	public boolean isFree(int n, Map<IB, Boolean> ctx) {
		return true;
	}

	@Override
	public IB sub(int v, C c, Map<IB, IB> ctx) {
		return this;
	}

	@Override
	public int getVar() {
		return -1;
	}

	@Override
	public BigInteger getNonFreeVars(Map<IB, BigInteger> ctx) {
		return BigInteger.ZERO;
	}
	
}
