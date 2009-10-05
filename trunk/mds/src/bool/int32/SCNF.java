package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class SCNF implements Expression {

    private List<SimpleConjunction> elements;

    public Expression invert() {
        SCNF r = new SCNF(new SimpleConjunction(new Const(1)));
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

    public SCNF optimize() {
        for (SimpleConjunction c1 : elements) {
            for (SimpleConjunction c2 : elements) {
                if (!c1.equals(c2) && c1.equalVars(c2)) {
                    List<SimpleConjunction> scnf = new ArrayList();
                    scnf.addAll(elements);
                    scnf.remove(c1);
                    scnf.remove(c2);
                    scnf.add(new SimpleConjunction(c1, c2));
                    return new SCNF(scnf).optimize();
                }
            }
        }
        List<SimpleConjunction> scnf = new ArrayList();
        for (SimpleConjunction c : elements) {
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

}
