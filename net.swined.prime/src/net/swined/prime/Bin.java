package net.swined.prime;

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
		if (a instanceof Var && b instanceof Var) {
			Var va = (Var) a;
			Var vb = (Var) b;
			if (va.name == vb.name)
				return a;
		}
//		if (a instanceof Var) {
//			Var va = (Var) a;
//			if (b.hasVar(va.name))
//				return and(a, b.sub(va.name, Const.ONE,
//						new HashMap<IExpression, IExpression>()));
//		}
//		if (b instanceof Var) {
//			Var vb = (Var) b;
//			if (a.hasVar(vb.name))
//				return and(b, a.sub(vb.name, Const.ONE,
//						new HashMap<IExpression, IExpression>()));
//		}
		return new And(a, b);
	}

	public static IExpression or(IExpression a, IExpression b) {
		if (a instanceof Const)
			return ((Const) a).or(b);
		if (b instanceof Const)
			return ((Const) b).or(a);
		if (a instanceof Var && b instanceof Var) {
			Var va = (Var) a;
			Var vb = (Var) b;
			if (va.name == vb.name)
				return a;
		}
//		if (a instanceof Var) {
//			Var va = (Var) a;
//			if (b.hasVar(va.name))
//				return or(a, b.sub(va.name, Const.ZERO,
//						new HashMap<IExpression, IExpression>()));
//		}
//		if (b instanceof Var) {
//			Var vb = (Var) b;
//			if (a.hasVar(vb.name))
//				return or(b, a.sub(vb.name, Const.ZERO,
//						new HashMap<IExpression, IExpression>()));
//		}
		return new Or(a, b);
	}

	public static IExpression xor(IExpression a, IExpression b) {
		if (a instanceof Const)
			return ((Const) a).xor(b);
		if (b instanceof Const)
			return ((Const) b).xor(a);
		if (a instanceof Var && b instanceof Var) {
			Var va = (Var) a;
			Var vb = (Var) b;
			if (va.name == vb.name)
				return Const.ZERO;
		}
		return new Xor(a, b);
	}

	public static IExpression m2(IExpression a, IExpression b, IExpression c) {
		return or(and(a, b), or(and(b, c), and(a, c)));
	}

	public static IExpression xor(IExpression a, IExpression b, IExpression c) {
		return xor(xor(a, b), c);
	}

	public static IExpression sub(IExpression x, int v, Const c) {
		return x.sub(v, Const.ONE, new WeakHashMap<IExpression, IExpression>());
	}
	
	public static IExpression sub(IExpression x, int v, IExpression s) {
		if (!x.hasVar(v))
			return x;
		IExpression a = sub(x, v, Const.ONE);
		IExpression b = sub(x, v, Const.ZERO);
		return or(and(s, a), and(not(s), b));
	}

}
