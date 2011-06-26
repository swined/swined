package net.swined.prime;

import java.util.Map;

public class BinOp implements IExpression {

	private final int var;
	private final BinOpType type;
	private final IExpression a;
	private final IExpression b;

	public BinOp(BinOpType type, IExpression a, IExpression b) {
		type.checkConstraints(a, b);
		this.var = a.getVar();
		this.type = type;
		this.a = a;
		this.b = b;
	}

	@Override
	public String toString() {
		return "(" + a + " " + type.sign + " " + b + ")";
	}

	@Override
	public IExpression sub(int v, IExpression c,
			Map<IExpression, IExpression> ctx) {
		IExpression s = ctx.get(this);
		if (s == null) {
			IExpression sa = a.sub(v, c, ctx);
			IExpression sb = b.sub(v, c, ctx);
			if (sa != a || sb != b)
				s = type.apply(sa, sb);
			else
				s = this;
			ctx.put(this, s);
		}
		return s;
	}

	@Override
	public int getVar() {
		return var;
	}

}
