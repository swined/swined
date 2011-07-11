package org.swined.fio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class LSOReader {

	private static final int SIZEOF_INT = 4;
	private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
	private static final long LSO_OFFSET_OFFSET = 0x34;
	private static final long MAP_CHUNK_SIZE = 1024 * 1024 * 1024; // 1Gb
	
	private static int getOffset(FileChannel chan) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(SIZEOF_INT);
		buffer.order(BYTE_ORDER);
		chan.read(buffer, LSO_OFFSET_OFFSET);
		buffer.flip();
		int offset = buffer.getInt();
		assert (chan.size() - offset) % LSORecord.LENGTH == 0;
		return offset;
	}
	
	private void reset() {
		// 
	}
	
	public void read(FileChannel chan) throws IOException {
		final LSORecord record = new LSORecord();
		long offset = getOffset(chan), length = MAP_CHUNK_SIZE;
		reset();
		while (offset < chan.size()) {
			if (offset + length > chan.size())
				length = chan.size() - offset;
			MappedByteBuffer buffer = chan.map(MapMode.READ_ONLY, offset, length);
			while (record.read(buffer))
				processRecord(record);
			offset += length;
		}
	}
	
	private void processRecord(LSORecord record) {
		System.out.println(record);
	}
	
}
