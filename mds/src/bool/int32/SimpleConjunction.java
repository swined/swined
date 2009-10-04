package bool.int32;

import java.util.HashSet;


public class SimpleConjunction {

    private Const coef;
    private HashSet<Variable> vars;

    public SimpleConjunction(Const c) {
        coef = new Const(c.getValue());
        vars = new HashSet();
    }

    public SimpleConjunction(Variable e) {
        coef = new Const(0xFFFFFFFF);
        vars = new HashSet();
        vars.add(e);
    }

    public SimpleConjunction(SimpleConjunction a, SimpleConjunction b) {
        coef = new Const(a.getCoef().getValue() & b.getCoef().getValue());
        vars = new HashSet();
        vars.addAll(a.getVars());
        vars.addAll(b.getVars());
    }

    public Const getCoef() {
        return coef;
    }

    public HashSet<Variable> getVars() {
        return new HashSet(vars);
    }

    public boolean equalVars(SimpleConjunction c) {
        HashSet<Variable> cv = c.getVars();
        return cv.containsAll(vars) && vars.containsAll(cv);
    }

    @Override
    public String toString() {
        String r = coef.toString();
        for (Variable v : vars) {
            r = r + " & " + v.toString();
        }
        return r;
    }

}
