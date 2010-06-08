package net.swined.parser.core;

public interface IMatchVisitor {

	boolean enterNode(IMatch match);
	void exitNode(IMatch match);
}
