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
		if (a == Const.ONE)
			return b;
		if (b == Const.ONE)
			return a;
		if (a instanceof L) {
			L l = (L)a;
			return L.get(l.v, And.get(l.a, b), And.get(l.b, b));
		}
		if (b instanceof L) {
			L l = (L)b;
			return L.get(l.v, And.get(l.a, a), And.get(l.b, a));
		}
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
}
