package org.swined.zpol.v1;

import java.util.Map;

public enum Const implements IB {

	ZERO {
		
		@Override
		public String toString() {
			return "0";
		}
		
	}, ONE {
		
		@Override
		public String toString() {
			return "1";
		}
		
	};

	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		return this;
	}
	
}
