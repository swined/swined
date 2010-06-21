package net.swined.parser.core.rules;

import java.util.ArrayList;
import java.util.List;

import net.swined.parser.core.Chain;
import net.swined.parser.core.Error;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.Util;

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
	public IMatch match(String source, int offset, boolean all)
			throws Exception {
		List<IMatch> matches = new ArrayList<IMatch>();
		for (IRule rule : rules) {
			IMatch match = Util.match(rule, source, offset, false);
			matches.add(match);
			offset = match.getEnd();
			source = source.substring(match.getEnd() - match.getStart());
		}
		if (all && !source.isEmpty())
			matches.add(new Error(id, offset, source));
		return new Chain(id, matches.toArray(new IMatch[0]));
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("[");
		int c = 0;
		for (IRule rule : rules) {
			if (c++ > 0)
				b.append(" ");
			b.append(rule);
		}
		b.append("]");
		return b.toString();
	}
}
