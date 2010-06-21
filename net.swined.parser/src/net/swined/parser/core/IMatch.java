package net.swined.parser.core;

public interface IMatch {

	String getRuleId();
	int getStart();
	int getEnd();
	int getErrCount();
	void accept(IMatchVisitor visitor);
	
}
