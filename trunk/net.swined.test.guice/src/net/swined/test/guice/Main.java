package net.swined.test.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main {

	public static void main(String[] args) {
		  Injector injector = Guice.createInjector(new TestModule());
		  injector.getInstance(IHelloWorld.class).run();
		  injector.getInstance(IHelloWorld.class).run();
	}

}
