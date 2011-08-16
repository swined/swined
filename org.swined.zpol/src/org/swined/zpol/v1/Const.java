package org.swined.zpol.v1;

import java.util.Map;

public enum Const implements IB {

	ZERO {
		
		@Override
		public String toString() {
			return "0";
		}

		@Override
		public boolean isOne() {
			return false;
		}

	};

	@Override
	public IB sub(int v, boolean c, Map<IB, IB> ctx) {
		return this;
	}

}
