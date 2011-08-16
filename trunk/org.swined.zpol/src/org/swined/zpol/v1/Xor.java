package org.swined.zpol.v1;


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
		if (a instanceof Vars && b instanceof Vars)
			if (((Vars)a).vars.equals(((Vars)b).vars))
				return Const.ZERO;
		return new Xor(a, b);
	}

	@Override
	public String toString() {
		return String.format("(%s + %s)", a, b);
	}

	@Override
	public void iterate(Iterator it) {
		a.iterate(it);
		b.iterate(it);
	}

}
