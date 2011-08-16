package org.swined.zpol.v1;


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
//		if (a instanceof Vars && b instanceof Xor) {
//			Xor xb = (Xor)b;
//			return Xor.get(get(a, xb.a), get(a, xb.b)); 
//		}
//		if (a instanceof Vars && b instanceof And) {
//			And xb = (And)b;
//			return get(get(a, xb.a), get(a, xb.b)); 
//		}
//		if (b instanceof Vars && a instanceof Xor) {
//			Xor xa = (Xor)a;
//			return Xor.get(get(b, xa.a), get(b, xa.b)); 
//		}
//		if (b instanceof Vars && a instanceof And) {
//			And xa = (And)a;
//			return get(get(b, xa.a), get(b, xa.b)); 
//		}
//		if (a instanceof Vars || b instanceof Vars)
//			throw new UnsupportedOperationException(String.format("%s & %s", a.getClass(), b.getClass()));
		return new And(a, b);
	}

	@Override
	public String toString() {
		return String.format("(%s & %s)", a, b);
	}

}
