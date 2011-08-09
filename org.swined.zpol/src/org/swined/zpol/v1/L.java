package org.swined.zpol.v1;

import java.util.Map;

public class L implements IB {

	public final int v;
	public final IB a;
	public final IB b;
	
	private L(int v, IB a, IB b) {
		this.v = v;
		this.a = a;
		this.b = b;
	}

	public static IB get(int v) {
		return new L(v, Const.ONE, Const.ZERO);
	}
	
	public static IB get(int v, IB a, IB b) {
		return new L(v, a, b);
	}

	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		if (this.v == v)
			return c ? a : b;
		IB r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = get(this.v, a.sub(v, c, ctx), b.sub(v, c, ctx)));
		return r;
	}
	
}
