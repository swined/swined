package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class SCNF {

    private List<SimpleConjunction> elements;

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

    public SCNF optimize() {
        List<SimpleConjunction> r = new ArrayList();
        for (SimpleConjunction i : elements) {
            r.add(i);
        }
        return new SCNF(r);
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
