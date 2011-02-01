package org.swined.dsa.util;

import java.math.BigInteger;

public class DsaPublicKey {

	public final BigInteger p;
	public final BigInteger q;
	public final BigInteger g;
	public final BigInteger y;
	
	public DsaPublicKey(BigInteger p, BigInteger q, BigInteger g, BigInteger y) {
		this.p = p;
		this.q = q;
		this.g = g;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "<p=" + p + ", q=" + q + ", g=" + g + ", y=" + y + ">";
	}
	
	public int guessPrivateKeyBitLength() {
		return q.bitLength();
	}
	
}
