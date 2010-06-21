package net.swined.parser.core;

public interface IRuleResolver {

	IRule getRule(String id) throws Exception;
	IRule[] getRules(String[] ids) throws Exception;
	
}
