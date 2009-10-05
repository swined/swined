package bool.int32;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SCNF implements Expression {

    private List<SimpleConjunction> elements;

    public Expression invert() {
        SCNF r = new SCNF(new SimpleConjunction(new Const(0xFFFFFFFF)));
        for (SimpleConjunction c : elements) {
            r = r.multiply(c.invert());
        }
        return r;
    }

    public Expression rotate(int s) {
        List<SimpleConjunction> scnf = new ArrayList();
        for (SimpleConjunction c : elements) {
            scnf.add(c.rotate(s));
        }
        return new SCNF(scnf);
    }

    public SCNF toSCNF() {
        return this;
    }

    public SCNF equalVarsOptimization() {
        List<SimpleConjunction> e = new ArrayList(elements);
        boolean optimized = true;
        while (optimized) {
            optimized = false;
            for (SimpleConjunction c1 : e) {
                for (SimpleConjunction c2 : e) {
                    if (!c1.equals(c2) && c1.equalVars(c2)) {
                        List<SimpleConjunction> scnf = new ArrayList();
                        scnf.addAll(e);
                        scnf.remove(c1);
                        scnf.remove(c2);
                        Const c = new Const(c1.getCoef().getValue() | c2.getCoef().getValue());
                        HashSet<Variable> v = new HashSet();
                        v.addAll(c1.getVars());
                        v.addAll(c2.getVars());
                        scnf.add(new SimpleConjunction(c, v));
                        e = scnf;
                        optimized = true;
                    }
                }
            }
        }
        return new SCNF(e);
    }

    public SCNF optimize() {
        List<SimpleConjunction> e = equalVarsOptimization().items();
        List<SimpleConjunction> scnf = new ArrayList();
        for (SimpleConjunction c : e) {
            SimpleConjunction o = c.optimize();
            if (!o.isZero())
                scnf.add(o);
        }
        return new SCNF(scnf);
    }

    public SCNF(SimpleConjunction c) {
        elements = new ArrayList();
        elements.add(c);
    }

    public SCNF(List<SimpleConjunction> elements) {
        this.elements = new ArrayList(elements);
    }

    public List<SimpleConjunction> items() {
        return new ArrayList(elements);
    }

    @Override
    public String toString() {
        if (isZero())
            return "0";
        String r = "";
        for (SimpleConjunction e : elements) {
            if (r.isEmpty()) {
                r = r + e.toString();
            } else {
                r = r + " | " + e.toString();
            }
        }
        return r;
    }

    public SCNF multiply(SCNF scnf) {
        List<SimpleConjunction> r = new ArrayList();
        for (SimpleConjunction c1 : elements) {
            for (SimpleConjunction c2 : scnf.items()) {
                r.add(new SimpleConjunction(c1, c2));
            }
        }
        return new SCNF(r);
    }

    public boolean isZero() {
        return this.optimize().items().isEmpty();
    }

}
