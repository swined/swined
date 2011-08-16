package org.swined.zpol.v1;

import java.math.BigInteger;


public interface IB {

	public interface Iterator {
	
		void process(BigInteger n);
		
	}
	
	void iterate(Iterator it);
	
}
