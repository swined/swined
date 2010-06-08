package net.swined.parser.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public IMatch match(IRuleResolver resolver, String source, int offset) {
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
