package net.swined.test.guice;

public class HelloMessageProvider implements IMessageProvider {

	@Override
	public String getMessage() {
		return "Hello, World!";
	}

	
	
}
