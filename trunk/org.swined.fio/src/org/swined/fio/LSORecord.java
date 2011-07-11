package org.swined.fio;

import java.nio.ByteBuffer;

public class LSORecord {

	public static final int LENGTH = 16;
	
	long timeGPS; // uint32
	int scanAngle1; // int32
	int scanAngle2; // int32
	int scanAngleDeviation; // uint16
	int returnsAmount; // 4
	int rangesAmount; // 4
	boolean scanDirection; // 1
	boolean scanEdge; // 1
	int scanLineCount; // 6

	public boolean read(ByteBuffer buf) {
		if (buf.remaining() < LENGTH)
			return false;
		timeGPS = uint(buf.getInt());
		scanAngle1 = buf.getInt();
		scanAngle2 = buf.getInt();
		scanAngleDeviation = ushort(buf.getShort());
		int t = ubyte(buf.get());
		returnsAmount = t >> 4;
		rangesAmount = t & 15;
		t = ubyte(buf.get());
		scanDirection = ((t & 128) != 0) ? true : false;
		scanEdge = ((t & 64) != 0) ? true : false;
		scanLineCount =t & 127;
		return true;
	}
	
	private static long uint(int i) {
		return (i + (1L << 32)) & ((1L << 32) - 1);
	}

	private static int ushort(short i) {
		return (i + (1 << 16)) & ((1 << 16) - 1);
	}
	
	private static int ubyte(byte i) {
		return (i + (1 << 8)) & ((1 << 8) - 1);
	}
	
	@Override
	public String toString() {
		return String.format("gt:%d a1:%d a2:%d ad:%d re:%d ra:%d sd:%s se:%s lc:%d",
				timeGPS,
				scanAngle1,
				scanAngle2,
				scanAngleDeviation,
				returnsAmount,
				rangesAmount,
				scanDirection ? "+" : "-",
				scanEdge ? "+" : "-",
				scanLineCount
				);
	}
	
}

