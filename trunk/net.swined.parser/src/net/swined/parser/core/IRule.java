package net.swined.parser.core;

public interface IRule {

	String getId();
	IMatch match(IRuleResolver resolver, String source, int offset) throws UnknownRuleException;
	
}
