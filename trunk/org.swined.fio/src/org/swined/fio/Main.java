package org.swined.fio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {

	public static void main(String args[]) throws IOException {
		File file = new File("c:/users/swined/ppp/missions/mmm/l0001-1-mmm/l0001-1-mmm.lso");
		new LSOReader().read(new FileInputStream(file).getChannel());
	}
	
}
