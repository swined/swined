package net.swined.parser.core;

import java.util.ArrayList;
import java.util.List;

public class ChainRule implements IRule {

	private final String id;
	private final IRule[] rules;
	
	public ChainRule(String id, IRule[] rules) {
		this.id = id;
		this.rules = rules;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public IMatch match(String source, int offset) {
		List<IMatch> matches = new ArrayList<IMatch>();
		for (IRule rule : rules) {
			IMatch match = rule.match(source, offset);
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
