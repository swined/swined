package bool.int32;

import java.util.ArrayList;
import java.util.BitSet;
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

    public Expression optimize() {
        List<SimpleDisjunction> sdnf = new ArrayList();
        for (SimpleDisjunction d : items) {
            if (d.isTrue()) {
                continue;
            }
            if (d.isFalse()) {
                return Const.create(new BitSet());
            }
            sdnf.add(d);
        }
        if (sdnf.isEmpty())
            return Const.create(Const.xFFFFFFFF());
        return this;
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

    //@Override
    public String _toString() {
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

}
