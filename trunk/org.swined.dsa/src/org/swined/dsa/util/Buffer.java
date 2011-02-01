package org.swined.dsa.util;

import java.math.BigInteger;

public class Buffer {

	private int pos;
	private final byte[] data;
	
	public Buffer(byte[] data) {
		this.pos = 0;
		this.data = data;
	}
	
	public byte[] get(int l) {
		if (l < 0)
			throw new IllegalArgumentException();
		if (data.length < pos + l) 
			throw new IllegalArgumentException();
		byte[] r = new byte[l];
		System.arraycopy(data, pos, r, 0, l);
		pos += l;
		return r;
	}
	
	public int getInt() {
		byte[] b = get(4);
		int[] d = new int[b.length];
		for (int i = 0; i < d.length; i++)
			d[i] = (b[i] + 0x100) % 0x100;
		return d[3] + (d[2] << 8) + (d[1] << 16) + (d[0] << 24);
	}
	
	public byte[] getStr() {
		return get(getInt());
	}
	
	public BigInteger getBigInt() {
		byte[] d = getStr();
		BigInteger r = BigInteger.ZERO;
		for (int i = 0; i < d.length; i++)
			r = r.add(BigInteger.valueOf((d[i] + 0x100) % 0x100).shiftLeft((d.length - i - 1) * 8));
		return r;
	}
	
	public DsaPublicKey readDsaPublicKey() {
		String type = new String(getStr());
		if (!"ssh-dss".equalsIgnoreCase(type))
			throw new IllegalArgumentException();
		BigInteger p = getBigInt();
		BigInteger q = getBigInt();
		BigInteger g = getBigInt();
		BigInteger y = getBigInt();
		if (pos < data.length)
			throw new IllegalArgumentException();
		return new DsaPublicKey(p, q, g, y);
	}

}
