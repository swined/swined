package net.swined.parser.core.rules;

import java.util.ArrayList;
import java.util.List;

import net.swined.parser.core.Chain;
import net.swined.parser.core.Error;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.Nothing;

public class RepeatRule implements IRule {

	private final String id;
	private final IRule rule;
	private final Integer min;
	private final Integer max;

	public RepeatRule(String id, IRule rule, Integer min, Integer max) {
		this.id = id;
		this.rule = rule;
		this.min = min;
		this.max = max;
	}

	@Override
	public String getId() {
		return id;
	}

	private static boolean gt(int a, Integer b) {
		if (b == null)
			return true;
		return a >= b;
	}

	private static boolean lt(int a, Integer b) {
		if (b == null)
			return true;
		return a <= b;
	}

	@Override
	public IMatch match(String source, int offset, boolean all)
			throws Exception {
		List<IMatch> matches = new ArrayList<IMatch>();
		while (true) {
			IMatch match = rule.match(source, offset, false);
			if (match == null)
				break;
			matches.add(match);
			offset = match.getEnd();
			source = source.substring(match.getEnd() - match.getStart());
			if (lt(matches.size(), max))
				break;
		}
		if (all && !source.isEmpty())
			matches.add(new Error(rule.getId(), offset, source));
		if (matches.size() == 0)
			return new Nothing(offset);
		else
			return new Chain(id, matches.toArray(new IMatch[0]));
	}

	@Override
	public String toString() {
		return "[" + rule + "]{" + (min == null ? "" : min) + ","
				+ (max == null ? "" : max) + "}";
	}

}
