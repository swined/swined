package net.swined.parser.core;

import java.util.ArrayList;
import java.util.List;

public class ChainRule implements IRule {

	private final String id;
	private final String[] rules;
	
	public ChainRule(String id, String[] rules) {
		this.id = id;
		this.rules = rules;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public IMatch match(IRuleResolver resolver, String source, int offset) throws UnknownRuleException {
		List<IMatch> matches = new ArrayList<IMatch>();
		for (String ruleId : rules) {
			IRule rule = resolver.getRule(ruleId);
			IMatch match = rule.match(resolver, source, offset);
			if (match == null) {
				return null;
			} else {
				matches.add(match);
				offset = match.getEnd();
				source = source.substring(match.getEnd() - match.getStart());
			}
		}
		return new Chain(id, matches.toArray(new IMatch[0]));
	}
	
}
