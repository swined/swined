package org.swined.zpol;

import java.util.Map;

public interface IB {

	boolean isFree(int n);
	IB sub(int v, C c, Map<IB, IB> ctx);
	
}
