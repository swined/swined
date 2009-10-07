package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class SDNF implements Expression {

    private List<SimpleDisjunction> items;

    public SDNF(List<SimpleDisjunction> items) {
        this.items = new ArrayList(items);
    }

    public boolean isTrue() {
        return false;
    }

    public boolean isFalse() {
        return false;
    }

    public SCNF invert() {
        List<SimpleConjunction> scnf = new ArrayList();
        for (SimpleDisjunction c : items)
            scnf.add(c.invert());
        return new SCNF(scnf);
    }

    public SDNF rotate(int s) {
        List<SimpleDisjunction> sdnf = new ArrayList();
        for (SimpleDisjunction c : items)
            sdnf.add(c.rotate(s));
        return new SDNF(sdnf);
    }

    public List<SimpleDisjunction> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String r = "";
        for (SimpleDisjunction c : items) {
            if (r.isEmpty()) {
                r += c.toString();
            } else {
                r += " & " + c.toString();
            }
        }
        return "[" + r + "]";
    }

    public Expression optimize() {
        return this;
    }

}
