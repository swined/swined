package net.swined.prime;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.Map;
import java.util.Set;

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
	public void getVars(BitSet vars, Set<IExpression> ctx) {
		vars.set(name);
	}
	
	@Override
	public BigInteger complexity(Map<IExpression, BigInteger> ctx) {
		return BigInteger.ONE;
	}
	
	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		return v == name ? c : this;
	}

	@Override
	public int getVar() {
		return name;
	}

	@Override
	public boolean hasVar(int v) {
		return v == name;
	}

}
