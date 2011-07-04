package net.swined.prime;

import java.util.Map;

public class Var implements IExpression {

	public final int name;

	public Var(int name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "x" + name;
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		return v == name ? c : this;
	}

	@Override
	public int getVar() {
		return name;
	}

}
