package net.swined.parser.core.rules;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.swined.parser.core.IMatch;
import net.swined.parser.core.IRule;
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
	public IMatch match(String source, int offset) {
		Matcher matcher = pattern.matcher(source);
		if (matcher.matches())
			return new Terminal(id, matcher.group(1), offset);
		else
			return null;
	}

	@Override
	public String toString() {
		return pattern.toString();
	}
	
}
