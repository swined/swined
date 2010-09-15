package net.swined.test.guice;

import com.google.inject.Inject;

public class HelloWorld implements IHelloWorld {

	private final IMessageProvider provider;
	private final IMessageWriter writer;
	
	@Inject
	public HelloWorld(IMessageProvider provider, IMessageWriter writer) {
		System.out.println("ctor");
		this.provider = provider;
		this.writer = writer;
	}
	
	@Override
	public void run() {
		writer.writeMessage(provider.getMessage());
	}

}
