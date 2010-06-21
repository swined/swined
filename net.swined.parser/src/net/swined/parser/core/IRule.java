package net.swined.parser.core;

public interface IRule {

	String getId();
	IMatch match(String source, int offset);
	
}
