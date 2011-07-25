package org.swined.zpol;

import java.math.BigInteger;
import java.util.Map;

public class X implements IB {

	private final IB a;
	private final IB b;
	
	private X(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		if (a == C.ZERO)
			return b;
		if (b == C.ZERO)
			return a;
		if (a == C.ONE && b == C.ONE)
			return C.ZERO;
		return new X(a, b);
	}

	@Override
	public boolean isFree(int n, Map<IB, Boolean> ctx) {
		Boolean r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = a.isFree(n, ctx) || b.isFree(n, ctx));
		return r;
	}

	@Override
	public IB sub(int v, C c, Map<IB, IB> ctx) {
		IB r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = get(a.sub(v, c, ctx), b.sub(v, c, ctx)));
		return r;
	}

	@Override
	public int getVar() {
		return a == C.ONE ? b.getVar() : a.getVar();
	}

	@Override
	public BigInteger getNonFreeVars(Map<IB, BigInteger> ctx) {
		BigInteger r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = a.getNonFreeVars(ctx).and(b.getNonFreeVars(ctx)));
		return r;
	}
	
}
