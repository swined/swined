package org.swined.zpol.v1;

import java.util.Map;

public class Xor implements IB {

	public final IB a;
	public final IB b;
	
	private Xor(IB a, IB b) {
		this.a = a;
		this.b = b;
	}
	
	public static IB get(IB a, IB b) {
		if (a == Const.ZERO)
			return b;
		if (b == Const.ZERO)
			return a;
		return new Xor(a, b);
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
		return String.format("(%s + %s)", a, b);
	}

	@Override
	public boolean isOne() {
		return false;
	}

}
