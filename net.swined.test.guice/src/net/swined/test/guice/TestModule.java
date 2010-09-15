package net.swined.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IHelloWorld.class).to(HelloWorld.class).in(Scopes.SINGLETON);
		bind(IMessageWriter.class).to(StdoutMessageWriter.class).in(Scopes.SINGLETON);
		bind(IMessageProvider.class).to(HelloMessageProvider.class).in(Scopes.SINGLETON);
	}

}
