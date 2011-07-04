package net.swined.prime;

import java.util.WeakHashMap;

public class Bin {

	public static IExpression not(IExpression e) {
		if (e instanceof Const)
			return ((Const) e).not();
		if (e instanceof Not)
			return ((Not) e).e;
		if (e instanceof BitMap) {
			BitMap be = (BitMap)e;
			return BitMap.create(be.block, ~be.map);
		}
		return new Not(e);
	}

	public static IExpression or(IExpression a, IExpression b) {
		return not(And.create(not(a), not(b)));
	}

	public static IExpression xor(IExpression a, IExpression b) {
		return or(And.create(a, not(b)), And.create(not(a), b));
	}

	public static IExpression m2(IExpression a, IExpression b, IExpression c) {
		return or(And.create(a, b), or(And.create(b, c), And.create(a, c)));
	}

	public static IExpression xor(IExpression a, IExpression b, IExpression c) {
		return xor(xor(a, b), c);
	}

	public static IExpression sub(IExpression x, int v, Const c) {
		return x.sub(v, c, new WeakHashMap<IExpression, IExpression>());
	}

	public static IExpression sub(IExpression x, int v, IExpression s) {
		IExpression a = sub(x, v, Const.ONE);
		IExpression b = sub(x, v, Const.ZERO);
		return or(And.create(s, a), And.create(not(s), b));
	}

	public static IExpression exclude(IExpression x, int v) {
		IExpression a = sub(x, v, Const.ONE);
		IExpression b = sub(x, v, Const.ZERO);
		return or(a, b);
	}

}
