package bool.int32;

import java.util.HashSet;

public class SimpleConjunction implements Expression {

    private Const coef;
    private HashSet<Variable> vars;

    public SimpleConjunction rotate(int s) {
        Const rc = coef.rotate(s);
        HashSet<Variable> rv = new HashSet();
        for (Variable v : vars) {
            rv.add(v.rotate(s));
        }
        return new SimpleConjunction(rc, rv);
    }

    public SimpleConjunction(Const c) {
        coef = Const.create(c.getValue());
        vars = new HashSet();
    }

    public SimpleConjunction(Variable e) {
        coef = Const.create(0xFFFFFFFF);
        vars = new HashSet();
        vars.add(e);
    }

    public SimpleConjunction(Const c, HashSet<Variable> e) {
        coef = Const.create(c.getValue());
        vars = new HashSet();
        vars.addAll(e);
    }

    public SimpleConjunction(SimpleConjunction a, SimpleConjunction b) {
        coef = Const.create(a.getCoef().getValue() & b.getCoef().getValue());
        if (coef.isFalse()) {
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
        return coef.getValue() == 0;
    }

    public boolean isTrue() {
        return coef.getValue() == -1 && vars.size() == 0;
    }

    @Override
    public String toString() {
        String r = coef.toString();
        for (Variable v : vars) {
            if (r.isEmpty()) {
                r += v.toString();
            } else {
                r += " & " + v.toString();
            }
        }
        return "[" + r + "]";
    }

    public Expression optimize() {
        if (vars.size() == 0)
            return coef;
        if (coef.isTrue() && vars.size() == 1)
            return (Variable)vars.toArray()[0];
        return this;
    }

    public Expression invert() {
        HashSet<Variable> d = new HashSet();
        for (Variable v : vars)
            d.add(v.invert());
        return new SimpleDisjunction(coef.invert(), d);
    }

}
