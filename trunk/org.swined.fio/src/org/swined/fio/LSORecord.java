package org.swined.fio;

import java.nio.ByteBuffer;

public class LSORecord {

	public final int LENGTH = 16;
	
	int timeGPS; // 32
	int scanAngle1; // 32
	int scanAngle2; // 32
	short scanAngleDeviation; // 16
	byte returnsAmount; // 4
	byte rangesAmount; // 4
	boolean scanDirection; // 1
	boolean scanEdge; // 1
	byte scanLineCount; // 6

	public boolean read(ByteBuffer buf) {
		if (buf.remaining() < LENGTH)
			return false;
		timeGPS = buf.getInt();
		scanAngle1 = buf.getInt();
		scanAngle2 = buf.getInt();
		scanAngleDeviation = buf.getShort();
		char t = buf.getChar();
		returnsAmount = (byte)(t >> 4);
		rangesAmount = (byte)(t & 15);
		t = buf.getChar();
		scanDirection = ((t & 128) > 0) ? true : false;
		scanEdge = ((t & 64) > 0) ? true : false;
		scanLineCount = (byte)(t & 127);
		return true;
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

