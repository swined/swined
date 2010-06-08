package net.swined.parser.core;

public interface IMatch {

	String getRuleId();
	int getStart();
	int getEnd();
	void accept(IMatchVisitor visitor);
	
}
