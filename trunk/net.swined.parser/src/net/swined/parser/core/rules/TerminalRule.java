package net.swined.parser.core.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.swined.parser.core.Error;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.Terminal;

public class TerminalRule implements IRule {

	private final String id;
	private final Pattern pattern;
	
	public TerminalRule(String id, String pattern) {
		this.id = id;
		this.pattern = Pattern.compile("^(" + pattern + ").*", Pattern.DOTALL);
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public IMatch match(String source, int offset, boolean all) {
		Matcher matcher = pattern.matcher(source);
		if (matcher.matches()) {
			if (all) {
				IMatch term = new Terminal(id, matcher.group(1), offset);
				if (matcher.group(1).length() == source.length())
					return term;
				else
					return new Error(id, offset, source);
			} else
				return new Terminal(id, matcher.group(1), offset);
		} else {
			if (all)
				return new Error(id, offset, source);
			else
				return new Error(id, offset, source.isEmpty() ? "" : source
						.substring(0, 1));
		}
	}

	@Override
	public String toString() {
		return pattern.toString();
	}
	
}
