package org.swined.zpol.v1;

import java.util.Map;

public interface IB {

	IB sub(int v, boolean c, Map<IB, IB> ctx);
	boolean isOne();
	
}
