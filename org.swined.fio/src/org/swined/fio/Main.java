package org.swined.fio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String args[]) throws IOException {
		File file = new File("/home/sw/wtf.lso");
		new LSOReader().read(new FileInputStream(file).getChannel());
	}
	
}
