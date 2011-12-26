package org.swined.g2r;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Utils {

	private Utils() {}
	
	public static byte[] read(InputStream stream) throws IOException {
		try {
			byte[] data = new byte[0];
			byte[] buffer = new byte[1024]; 
			while (true) {
				int c = stream.read(buffer);
				if (c < 0)
					break;
				data = Arrays.copyOf(data, data.length + c);
				System.arraycopy(buffer, 0, data, data.length - c, c);
			}
			return data;
		} finally {
			stream.close();
		}
	}
	
}
