package net.swined.parser.core.rules;

import net.swined.parser.core.Chain;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.IRule;

public class OrRule implements IRule {

	private final String id;
	private final IRule[] rules;
	
	public OrRule(String id, IRule[] rules) {
		this.id = id;
		this.rules = rules;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public IMatch match(String source, int offset) {
		for (IRule rule : rules) {
			IMatch match = rule.match(source, offset);
			if (match != null)
				return new Chain(id, new IMatch[] { match });
		}
		return null;
	}

	@Override
	public String toString() {
		String r = rules[0].getId();
		for (int i = 1; i < rules.length; i++)
			r += " | " + rules[i].getId();
		return r;
	}
	
}
