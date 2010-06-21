package net.swined.parser.demo;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.RuleResolver;
import net.swined.parser.core.rules.ChainRule;
import net.swined.parser.core.rules.IRule;
import net.swined.parser.core.rules.OrRule;
import net.swined.parser.core.rules.RepeatRule;
import net.swined.parser.core.rules.TerminalRule;

public class Main {

	public static void main(String[] args) throws Exception {
		String[][] terms = new String[][] {
				{"nothing", ""},
				{"eol", "\n"},
				{"eof", "$"},
 { "semicolon", ";" },
				{"ws", " +"},
				{"rx", "[^\n]*"},
				{"kw_term", "term"},
				{"kw_def", "def"},
				{"id", "[a-zA-Z0-9_]+"},
				{"op_eq", "="},
				{"op_and", "&"},
				{"op_or", "\\|"}
		};
		RuleResolver resolver = new RuleResolver();		
		for (String[] term : terms)
			resolver.add(new TerminalRule(term[0], term[1]));
		resolver.add(new OrRule("eo", resolver.getRules(new String[] { "eol",
				"eof", "semicolon" })));
		resolver.add(new OrRule("eo!f", resolver.getRules(new String[] { "eol",
				"semicolon" })));
		resolver.add(new OrRule("ws?", resolver.getRules(new String[] {"ws", "nothing"} )));
		resolver.add(new ChainRule("ID", resolver.getRules(new String[] { "ws?", "id", "ws?" })));		
		resolver.add(new ChainRule("term_rule", resolver.getRules(new String[] { "kw_term", "ws", "ID", "op_eq", "rx", "eo" })));
		resolver.add(
new ChainRule("or", new IRule[] {
				resolver.getRule("ID"),
 new RepeatRule(
				null, new ChainRule(null, new IRule[] {
						resolver.getRule("op_or"), resolver.getRule("ID") }),
				null, null)
				}));
		resolver.add(new ChainRule("or_rule", resolver.getRules(new String[] {
				"kw_def", "ws", "ID", "op_eq", "or", "eo" })));
		resolver.add(new ChainRule("and", new IRule[] {
				resolver.getRule("ID"),
				new RepeatRule(null, new ChainRule(null, new IRule[] {
						resolver.getRule("op_and"), resolver.getRule("ID") }),
						null, null) }));
		resolver.add(new ChainRule("chain_rule", resolver
				.getRules(new String[] { "kw_def", "ws", "ID", "op_eq", "and",
						"eo" })));
		resolver.add(new OrRule("rule", resolver.getRules(new String[] {
				"term_rule", "or_rule", "chain_rule" })));
		resolver.add(new RepeatRule("ruleset", new OrRule(null, resolver
				.getRules(new String[] { "rule", "eo!f" })), null,
				null));
		IMatch match = resolver.getRule("ruleset").match(
				"def 123 = asdf | asd as 8",
				0, true);
		match.accept(new Visitor());
	}
	
}
