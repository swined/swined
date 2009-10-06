package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class SCNF implements Expression {

    private List<SimpleConjunction> items;

    public SCNF(List<SimpleConjunction> items) {
        this.items = new ArrayList(items);
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public Expression optimize() {
        return this;
    }

    public SDNF invert() {
        List<SimpleDisjunction> sdnf = new ArrayList();
        for (SimpleConjunction c : items)
            sdnf.add(c.invert());
        return new SDNF(sdnf);
    }

    public SCNF rotate(int s) {
        List<SimpleConjunction> scnf = new ArrayList();
        for (SimpleConjunction c : items)
            scnf.add(c.rotate(s));
        return new SCNF(scnf);
    }

    @Override
    public String toString() {
        String r = "";
        for (SimpleConjunction c : items) {
            if (r.isEmpty()) {
                r += c.toString();
            } else {
                r += " | " + c.toString();
            }
        }
        return "{" + r + "}";
    }

}
