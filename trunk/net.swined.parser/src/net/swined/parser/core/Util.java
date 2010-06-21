package net.swined.parser.core;

import net.swined.parser.core.rules.IRule;

public class Util {

	public static IMatch match(IRule rule, String source, int offset,
			boolean all)
			throws Exception {
		if (source.isEmpty())
			return rule.match(source, offset, all);
		IMatch m = null;
		for (int i = 0; i < source.length(); i++) {
			IMatch n = rule.match(source.substring(i), offset + i, all);
			if (m == null)
				m = n;
			if (m.getErrCount() + m.getStart() > n.getErrCount() + n.getStart())
				m = n;
		}
		if (m.getStart() == offset) {
			return m;
		} else {
			IMatch e = new Error(rule.getId(), offset, source.substring(0, m
					.getStart()
					- offset));
			return new Chain(null, new IMatch[] { e, m });
		}
	}

}
