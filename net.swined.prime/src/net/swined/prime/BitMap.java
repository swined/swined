package net.swined.prime;

import java.math.BigInteger;
import java.util.Map;

public class BitMap implements IExpression {

	private static final int BLOCK_SIZE = 4;
	private static final long ZERO = 0L;
	private static final long ONE = pow(2, pow(2, BLOCK_SIZE)) - 1;
	private static final long[] VARS = varMaps();
	
	public final int block;
	public final long map;

	private BitMap(int block, long map) {
		this.block = block;
		this.map = map;
	}

	public static IExpression create(int block, long map) {
		if (map == ZERO)
			return Const.ZERO;
		if (map == ONE)
			return Const.ONE;
		return new BitMap(block, map & ONE);
	}
	
	private static long[] varMaps() {
		long[] maps = new long[BLOCK_SIZE];
		for (int i = 0; i < pow(2, BLOCK_SIZE); i++)
			for (int o = 0; o < BLOCK_SIZE; o++)
				maps[BLOCK_SIZE - o - 1] |= ((i >> o) & 1) << i;
		return maps;
	}

	private static int pow(int e, int p) {
		return BigInteger.valueOf(e).pow(p).intValue();
	}
	
	public static IExpression var(int name) {
		return create(name / BLOCK_SIZE, VARS[name % BLOCK_SIZE]);
	}
	
	@Override
	public String toString() {
		return block + "/" + Long.toBinaryString(map);
	}

	@Override
	public IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
		if (v / BLOCK_SIZE != block)
			return this;
		int o = v % BLOCK_SIZE;
		int l = pow(2, BLOCK_SIZE - o - 1);
		switch (c) {
		case ZERO: return create(block, (map & ~VARS[o]) | ((map & ~VARS[o]) << l));
		case ONE: return create(block, (map & VARS[o]) | ((map & VARS[o]) >> l));
		default: throw new UnsupportedOperationException();
		}
	}

	private boolean hasVar(int o) {
		int l = pow(2, BLOCK_SIZE - o - 1);
		return ((map & VARS[o]) >> l) != (map & ~VARS[o]);
	}
	
	@Override
	public int getVar() {
		for (int i = 0; i < BLOCK_SIZE; i++)
			if (hasVar(i))
				return i + block * BLOCK_SIZE;
		return -1;
	}

}