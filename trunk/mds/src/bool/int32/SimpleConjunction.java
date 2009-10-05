package bool.int32;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SimpleConjunction implements Expression {

    private Const coef;
    private HashSet<Variable> vars;

    public SCNF invert() {
        List<SimpleConjunction> scnf = new ArrayList();
        scnf.add(new SimpleConjunction(coef.invert()));
        for (Variable v : vars) {
            scnf.add(new SimpleConjunction(v.invert()));
        }
        return new SCNF(scnf);
    }

    public SimpleConjunction rotate(int s) {
        Const rc = coef.rotate(s);
        HashSet<Variable> rv = new HashSet();
        for (Variable v : vars) {
            rv.add(v.rotate(s));
        }
        return new SimpleConjunction(rc, rv);
    }

    public SCNF toSCNF() {
        return new SCNF(this);
    }

    public SimpleConjunction(Const c) {
        coef = new Const(c.getValue());
        vars = new HashSet();
    }

    public SimpleConjunction(Variable e) {
        coef = new Const(0xFFFFFFFF);
        vars = new HashSet();
        vars.add(e);
    }

    public SimpleConjunction(Const c, HashSet<Variable> e) {
        coef = new Const(c.getValue());
        vars = new HashSet();
        vars.addAll(e);
    }

    public SimpleConjunction(SimpleConjunction a, SimpleConjunction b) {
        coef = new Const(a.getCoef().getValue() & b.getCoef().getValue());
        vars = new HashSet();
        if (!coef.isZero()) {
            vars.addAll(a.getVars());
            vars.addAll(b.getVars());
        }
    }

    public SimpleConjunction optimize() {
        for (Variable v : vars) {
            if (vars.contains(v.invert()))
                return new SimpleConjunction(new Const(0));
        }
        return this;
    }

    public Const getCoef() {
        return coef;
    }

    public HashSet<Variable> getVars() {
        return vars;
    }

    public boolean equalVars(SimpleConjunction c) {
        HashSet<Variable> cv = c.getVars();
        return cv.containsAll(vars) && vars.containsAll(cv);
    }

    public boolean isZero() {
        return coef.getValue() == 0;
    }

    @Override
    public String toString() {
        String r = coef.getValue() == -1 ? "" : coef.toString();
        for (Variable v : vars) {
            if (r.isEmpty()) {
                r += v.toString();
            } else {
                r += " & " + v.toString();
            }
        }
        return r;
    }

}
