package net.swined.prime;

import java.util.Map;

public class And implements IExpression {

	public final IExpression a;
	public final IExpression b;

	private And(IExpression a, IExpression b) {
		this.a = a;
		this.b = b;
	}

	public static IExpression create(IExpression a, IExpression b) {
		if (a instanceof Const)
			return ((Const) a).and(b);
		if (b instanceof Const)
			return ((Const) b).and(a);
		if (a instanceof BitMap && b instanceof BitMap) {
			BitMap ba = (BitMap)a;
			BitMap bb = (BitMap)b;
			if (ba.block == bb.block)
				return BitMap.create(ba.block, ba.map & bb.map);
		}
		return new And(a, b);
	}
	
	@Override
	public String toString() {
		return "(" + a + " & " + b + ")";
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			s = And.create(a.sub(v, c, ctx), b.sub(v, c, ctx));
			ctx.put(this, s);
		}
		return s;
	}

	@Override
	public int getVar() {
		return a.getVar();
	}

}
