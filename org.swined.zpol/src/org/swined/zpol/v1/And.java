package org.swined.zpol.v1;

import java.util.Map;

public class And implements IB {

	public final IB a;
	public final IB b;
	
	private And(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		if (a == Const.ZERO || b == Const.ZERO)
			return Const.ZERO;
		if (a instanceof Vars && b instanceof Vars)
			return Vars.get(((Vars)a).vars.or(((Vars)b).vars));
		return new And(a, b);
	}

	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		IB r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = get(a.sub(v, c, ctx), b.sub(v, c, ctx)));
		return r;
	}
	
	@Override
	public String toString() {
		return String.format("(%s & %s)", a, b);
	}

	@Override
	public Poly toPoly(int limit, Map<IB, Poly> ctx) {
		Poly r = ctx.get(this);
		if (r == null)
			ctx.put(this, r = Poly.and(a.toPoly(limit, ctx), b.toPoly(limit, ctx)).limit(limit));
		return r;
	}
}
