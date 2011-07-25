package org.swined.zpol;

import java.util.Map;

public interface IB {

	int getVar();
	boolean isFree(int n, Map<IB, Boolean> ctx);
	IB sub(int v, C c, Map<IB, IB> ctx);
	
}
