package net.swined.parser.demo;
import net.swined.parser.core.IMatch;
import net.swined.parser.core.IRule;
import net.swined.parser.core.RuleResolver;
import net.swined.parser.core.rules.ChainRule;
import net.swined.parser.core.rules.OrRule;
import net.swined.parser.core.rules.RepeatRule;
import net.swined.parser.core.rules.RuleRef;
import net.swined.parser.core.rules.TerminalRule;

public class Main {

	public static void main(String[] args) throws Exception {
		String[][] terms = new String[][] {
				{"nothing", ""},
				{"eol", "\n"},
				{"eof", "$"},
				{"colon", ";"},
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
		resolver.add(new OrRule("eo", resolver.getRules(new String[] {"eol", "eof", "colon" })));
		resolver.add(new OrRule("eo!f", resolver.getRules(new String[] { "eol", "colon" })));
		resolver.add(new OrRule("ws?", resolver.getRules(new String[] {"ws", "nothing"} )));
		resolver.add(new OrRule("and", new IRule[] { new RuleRef(resolver, "and_chain"), new RuleRef(resolver, "ID") }));
		resolver.add(new ChainRule("ID", resolver.getRules(new String[] { "ws?", "id", "ws?" })));		
		resolver.add(new ChainRule("and_chain", resolver.getRules(new String[] { "ID", "op_and", "and" })));
		resolver.add(new ChainRule("term_rule", resolver.getRules(new String[] { "kw_term", "ws", "ID", "op_eq", "rx", "eo" })));
		resolver.add(new ChainRule("chain_rule", resolver.getRules(new String[] { "kw_def", "ws", "ID", "op_eq", "and", "eo" })));
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
		resolver.add(new OrRule("rule", resolver.getRules(new String[] {
				"term_rule", "or_rule", "chain_rule" })));
		resolver.add(new OrRule("rule?", resolver.getRules(new String[] {
				"rule", "eo!f" })));
		resolver.add(new RepeatRule("ruleset", resolver.getRule("rule?"), null,
				null));
		IMatch match = resolver.getRule("ruleset").match("def wtf = 42 & asd;\nterm x = aasdf\ndef 123 = asdf | asdf", 0);
		// match.accept(new Visitor());
		System.out.println(resolver);
	}
	
}
