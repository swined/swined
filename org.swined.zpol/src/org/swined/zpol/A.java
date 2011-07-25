package org.swined.zpol;

import java.util.Map;

public class A implements IB {

	private final IB a;
	private final IB b;
	
	private A(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		if (a == C.ZERO || b == C.ZERO)
			return C.ZERO;
		if (a == C.ONE)
			return b;
		if (b == C.ONE)
			return a;
		return new A(a, b);
	}

	@Override
	public boolean isFree(int n) {
		return a.isFree(n) && b.isFree(n);
	}

	@Override
	public IB sub(int v, C c, Map<IB, IB> ctx) {
		IB r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = get(a.sub(v, c, ctx), b.sub(v, c, ctx)));
		return r;
	}
	
}
