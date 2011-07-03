package net.swined.prime;

import java.util.Map;

public class WTF implements IExpression {

	public static final WTF GET = new WTF();
	
	private WTF() {
	}

	@Override
	public String toString() {
		return "wtf";
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		return this;
	}

	@Override
	public IExpression wxsub(int v, Map<IExpression, IExpression> ctx) {
		return this;
	}
	
	@Override
	public int getVar() {
		return -1;
	}

	@Override
	public boolean hasVar(int v) {
		return false;
	}

}
