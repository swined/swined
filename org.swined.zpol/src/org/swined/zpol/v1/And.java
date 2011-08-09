package org.swined.zpol.v1;

public class And implements IB {

	public final Var var;
	public final IB ib;
	
	private And(Var var, IB ib) {
		this.var = var;
		this.ib = ib;
	}
	
}
