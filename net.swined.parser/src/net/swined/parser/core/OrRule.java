package net.swined.parser.core;

public class OrRule implements IRule {

	private final String id;
	private final String[] rules;
	
	public OrRule(String id, String[] rules) {
		this.id = id;
		this.rules = rules;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public IMatch match(IRuleResolver resolver, String source, int offset) throws UnknownRuleException {
		for (String ruleId : rules) {
			IRule rule = resolver.getRule(ruleId);
			IMatch match = rule.match(resolver, source, offset);
			if (match != null)
				return new Chain(id, new IMatch[] { match });
		}
		return null;
	}

	@Override
	public String toString() {
		String r = rules[0];
		for (int i = 1; i < rules.length; i++)
			r += " | " + rules[i];
		return r;
	}
	
}
