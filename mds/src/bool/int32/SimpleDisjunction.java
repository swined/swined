package bool.int32;

import java.util.HashSet;

public class SimpleDisjunction implements Expression {

    private Const coef;
    private HashSet<Variable> vars;

    public SimpleDisjunction rotate(int s) {
        Const rc = coef.rotate(s);
        HashSet<Variable> rv = new HashSet();
        for (Variable v : vars) {
            rv.add(v.rotate(s));
        }
        return new SimpleDisjunction(rc, rv);
    }

    public SimpleDisjunction(Const c) {
        coef = new Const(c.getValue());
        vars = new HashSet();
    }

    public SimpleDisjunction(Variable e) {
        coef = new Const(0);
        vars = new HashSet();
        vars.add(e);
    }

    public SimpleDisjunction(Const c, HashSet<Variable> e) {
        coef = new Const(c.getValue());
        vars = new HashSet();
        vars.addAll(e);
    }

    public SimpleDisjunction(SimpleDisjunction a, SimpleDisjunction b) {
        coef = new Const(a.getCoef().getValue() | b.getCoef().getValue());
        if (coef.isTrue()) {
            vars = new HashSet();
        } else {
            vars = new HashSet(a.getVars());
            vars.addAll(b.getVars());
        }
    }

    public Const getCoef() {
        return coef;
    }

    public HashSet<Variable> getVars() {
        return vars;
    }

    public boolean isFalse() {
        return coef.getValue() == 0 && vars.size() == 0;
    }

    public boolean isTrue() {
        return coef.getValue() == -1;
    }

    @Override
    public String toString() {
        String r = coef.toString();
        for (Variable v : vars) {
            if (r.isEmpty()) {
                r += v.toString();
            } else {
                r += " | " + v.toString();
            }
        }
        return "{" + r + "}";
    }

    public Expression optimize() {
        return this;
    }

    public Expression invert() {
        HashSet<Variable> d = new HashSet();
        for (Variable v : vars)
            d.add(v.invert());
        return new SimpleConjunction(coef.invert(), d);
    }

}