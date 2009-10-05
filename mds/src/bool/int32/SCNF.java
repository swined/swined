package bool.int32;

import java.util.ArrayList;
import java.util.Collections;
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
                    if (c1.equals(c2))
                        continue;
                    if (c1.equalVars(c2)) {
                        e.remove(c1);
                        e.remove(c2);
                        Const c = new Const(c1.getCoef().getValue() | c2.getCoef().getValue());
                        e.add(new SimpleConjunction(c, new HashSet(c1.getVars())));
                        optimized = true;
                        break;
                    }
                    if (c1.getCoef().getValue() == c2.getCoef().getValue())
                        if (c1.getVars().containsAll(c2.getVars())) {
                            e.remove(c1);
                            optimized = true;
                            break;
                        }
                }
                if (optimized)
                    break;
            }
        }
        return new SCNF(e);
    }

    public SCNF zeroOptimization() {
        boolean optimized = true;
        List<SimpleConjunction> e = new ArrayList(elements);
        while (optimized) {
            optimized = false;
            for (SimpleConjunction c : e) {
                SimpleConjunction o = c.optimize();
                if (o.isZero()) {
                    e.remove(c);
                    optimized = true;
                    break;
                }
            }
        }
        return new SCNF(e);
    }

    public SCNF optimize() {
        return this;
        //return equalVarsOptimization().zeroOptimization();
    }

    public SCNF(SimpleConjunction c) {
        List<SimpleConjunction> e = new ArrayList();
        e.add(c);
        elements = Collections.unmodifiableList(e);
    }

    public SCNF(List<SimpleConjunction> elements) {
        this.elements = Collections.unmodifiableList(elements);
    }

    public List<SimpleConjunction> items() {
        return elements;
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

    public SCNF multiply(Expression expr) {
        List<SimpleConjunction> r = new ArrayList();
        for (SimpleConjunction c1 : elements) {
            for (SimpleConjunction c2 : expr.toSCNF().items()) {
                r.add(new SimpleConjunction(c1, c2));
            }
        }
        return new SCNF(r);
    }

    public boolean isZero() {
        return this.optimize().items().isEmpty();
    }

}
