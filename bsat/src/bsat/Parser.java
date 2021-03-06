package bsat;

import java.util.LinkedList;
import java.util.List;

public class Parser {

    private interface Token {
    }

    private interface NoBrackets {
    }

    private class OpeningBracket implements Token {
    }

    private class ClosingBracket implements Token {
    }

    private class NotOp implements Token, NoBrackets {
    }

    private class AndOp implements Token, NoBrackets {
    }

    private class OrOp implements Token, NoBrackets {
    }

    private class Symbol implements Token, NoBrackets {

        private String name;

        public Symbol(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private class Expr implements Token, NoBrackets {

        private Expression expr;

        public Expr(Expression expr) {
            this.expr = expr;
        }

        public Expression getExpr() {
            return expr;
        }
    }

    private List<Token> tokenize(String string) throws SyntaxErrorException {
        int i = 0;
        List<Token> tokens = new LinkedList();
        while (i < string.length()) {
            char c = string.charAt(i);
            switch (c) {
                case ' ':
                    i++;
                    continue;
                case '(':
                    i++;
                    tokens.add(new OpeningBracket());
                    continue;
                case ')':
                    i++;
                    tokens.add(new ClosingBracket());
                    continue;
                case '!':
                    i++;
                    tokens.add(new NotOp());
                    continue;
                case '&':
                    i++;
                    tokens.add(new AndOp());
                    continue;
                case '|':
                    i++;
                    tokens.add(new OrOp());
                    continue;
            }
            if (Character.isJavaIdentifierStart(c)) {
                String s = new String();
                s += c;
                i++;
                while ((i < string.length()) && (Character.isJavaIdentifierPart(string.charAt(i)))) {
                    s += string.charAt(i);
                    i++;
                }
                tokens.add(new Symbol(s));
                continue;
            }
            throw new SyntaxErrorException("unexpected character: " + c);
        }
        return tokens;
    }

    private Expression parseBracketless(List<NoBrackets> tokens) throws SyntaxErrorException {
        if (tokens.size() == 0) {
            throw new SyntaxErrorException("trying to parse empty token set");
        }
        if (tokens.size() == 1) {
            NoBrackets token = tokens.get(0);
            if (token instanceof Symbol) {
                Symbol symbol = (Symbol) token;
                return new Variable(symbol.getName());
            }
            if (token instanceof Expr) {
                Expr expr = (Expr) token;
                return expr.getExpr();
            }
        }
        for (NoBrackets token : tokens) {
            if (token instanceof OrOp) {
                int index = tokens.indexOf(token);
                if ((index == 0) | (index == tokens.size() - 1)) {
                    throw new SyntaxErrorException("operator not expected");
                }
                List<NoBrackets> part1 = tokens.subList(0, index);
                List<NoBrackets> part2 = tokens.subList(index + 1, tokens.size());
                return new OrOperator(parseBracketless(part1), parseBracketless(part2));
            }
        }
        for (NoBrackets token : tokens) {
            if (token instanceof AndOp) {
                int index = tokens.indexOf(token);
                if ((index == 0) | (index == tokens.size() - 1)) {
                    throw new SyntaxErrorException("operator not expected");
                }
                List<NoBrackets> part1 = tokens.subList(0, index);
                List<NoBrackets> part2 = tokens.subList(index + 1, tokens.size());
                return new AndOperator(parseBracketless(part1), parseBracketless(part2));
            }
        }
        NoBrackets token = tokens.get(0);
        if (token instanceof NotOp) {
            return parseBracketless(tokens.subList(1, tokens.size())).negate();
        }
        throw new SyntaxErrorException("unexpected token");
    }

    private Expression parseTokens(List<Token> tokens) throws SyntaxErrorException {
        int b = 0;
        List<NoBrackets> result = new LinkedList<NoBrackets>();
        List<Token> inner = new LinkedList<Token>();
        for (Token t : tokens) {
            if (t instanceof OpeningBracket) {
                if (b > 0) {
                    inner.add(t);
                }
                b++;
                continue;
            }
            if (t instanceof ClosingBracket) {
                if (b == 0) {
                    throw new SyntaxErrorException("unexpected closing bracket");
                }
                b--;
                if (b == 0) {
                    result.add(new Expr(parseTokens(inner)));
                    inner.clear();
                } else {
                    inner.add(t);
                }
                continue;
            }
            if (b == 0) {
                result.add((NoBrackets) t);
            } else {
                inner.add(t);
            }
        }
        if (b > 0) {
            throw new SyntaxErrorException("closing bracket expected");
        }
        return parseBracketless(result);
    }

    private Expression parseString(String string) throws SyntaxErrorException {
        return parseTokens(tokenize(string));
    }

    public static Expression parse(String string) throws SyntaxErrorException {
        Parser p = new Parser();
        return p.parseString(string);
    }
}
