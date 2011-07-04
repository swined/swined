package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public enum Const implements IExpression {

	ONE {

		@Override
		public IExpression and(IExpression a) {
			return a;
		}

		@Override
		public Const not() {
			return ZERO;
		}

		@Override
		public String toString() {
			return "1";
		}

	},
	ZERO {

		@Override
		public IExpression and(IExpression a) {
			return ZERO;
		}

		@Override
		public Const not() {
			return ONE;
		}

		@Override
		public String toString() {
			return "0";
		}

	};

	@Override
	public BigInteger complexity(Map<IExpression, BigInteger> ctx) {
		return BigInteger.ZERO;
	}
	
	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		return this;
	}

	@Override
	public int getVar() {
		return -1;
	}

	public abstract Const not();

	public abstract IExpression and(IExpression a);

}
