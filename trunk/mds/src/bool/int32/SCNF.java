package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class SCNF {

    private List<SimpleConjunction> elements;

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
        return this;
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

}
