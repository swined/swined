package org.swined.zpol.v1;

import java.math.BigInteger;
import java.util.Map;

public interface IB {

	IB sub(int v, boolean c, Map<IB, IB> ctx);
	Poly toPoly(BigInteger mask, Map<IB, Poly> ctx);
}
