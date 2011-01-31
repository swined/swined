package net.swined.prime.binary;

import java.util.Map;


public class Xor extends Expression {

    public final IExpression a;
    public final IExpression b;

    public Xor(IExpression a, IExpression b) {
        super(a, b);
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "(" + a + " ^ " + b + ")";
    }

	@Override
	protected IExpression subImpl(int v, Const c,
			Map<IExpression, IExpression> ctx) {
		return Bin.xor(a.sub(v, c, ctx), b.sub(v, c, ctx));
	}
}
