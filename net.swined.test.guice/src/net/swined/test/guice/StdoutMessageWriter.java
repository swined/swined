package net.swined.test.guice;

public class StdoutMessageWriter implements IMessageWriter {

	@Override
	public void writeMessage(String message) {
		System.out.println(message);
	}

}
