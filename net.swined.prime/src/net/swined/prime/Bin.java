package net.swined.prime;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.HashSet;
import java.util.WeakHashMap;

public class Bin {

	public static IExpression not(IExpression e) {
		if (e instanceof Const)
			return ((Const) e).not();
		if (e instanceof Not)
			return ((Not) e).e;
		return new Not(e);
	}

	public static IExpression and(IExpression a, IExpression b) {
		if (a instanceof Const)
			return ((Const) a).and(b);
		if (b instanceof Const)
			return ((Const) b).and(a);
		return new And(a, b);
	}

	public static IExpression or(IExpression a, IExpression b) {
		return not(and(not(a), not(b)));
	}

	public static IExpression xor(IExpression a, IExpression b) {
		return or(and(a, not(b)), and(not(a), b));
	}

	public static IExpression m2(IExpression a, IExpression b, IExpression c) {
		return or(and(a, b), or(and(b, c), and(a, c)));
	}

	public static IExpression xor(IExpression a, IExpression b, IExpression c) {
		return xor(xor(a, b), c);
	}

	public static IExpression sub(IExpression x, int v, Const c) {
		return x.sub(v, c, new WeakHashMap<IExpression, IExpression>());
	}

	public static BigInteger getVars(IExpression x) {
		BitSet vars = new BitSet();
		x.getVars(vars, new HashSet<IExpression>());
		BigInteger r = BigInteger.ZERO;
		for (int i = 0; i < vars.length(); i++)
			if (vars.get(i))
				r = r.setBit(i);
		return r;
	}
	
	public static BigInteger complexity(IExpression x) {
		return x.complexity(new WeakHashMap<IExpression, BigInteger>());
	}
	
	public static IExpression sub(IExpression x, int v, IExpression s) {
		if (!x.hasVar(v))
			return x;
		IExpression a = sub(x, v, Const.ONE);
		IExpression b = sub(x, v, Const.ZERO);
		return or(and(s, a), and(not(s), b));
	}

	public static IExpression exclude(IExpression x, int v) {
		if (!x.hasVar(v))
			return x;
		IExpression a = sub(x, v, Const.ONE);
		IExpression b = sub(x, v, Const.ZERO);
		return or(a, b);
	}

}
