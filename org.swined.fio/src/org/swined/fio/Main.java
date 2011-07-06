package org.swined.fio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class Main {

	private static final long MAP_CHUNK_SIZE = 1024 * 1024 * 1024;
	
	public static void main(String args[]) throws IOException {
		File file = new File("/home/sw/ru_windows_7_professional_x86_dvd_x15-65834.iso");
		final long size = file.length();
		
		long sum = 0;
		long pos = 0;
		long time = System.currentTimeMillis();
		FileInputStream in = new FileInputStream(file);
		FileChannel chan = in.getChannel();
		while (pos < size) {
			System.out.println(100 * pos / size + "%");
			long cs = size - pos;
			if (cs > MAP_CHUNK_SIZE)
				cs = MAP_CHUNK_SIZE;
			MappedByteBuffer buf = chan.map(MapMode.READ_ONLY, pos, cs);
			while (buf.hasRemaining())
				sum += buf.getChar();
			pos += MAP_CHUNK_SIZE;
		}
		time = System.currentTimeMillis() - time;
		System.out.println(sum);
		System.out.println(1000 * size / time / 1024 / 1024 + " mb/s");
	}
	
}
