package bool.int32;

import java.util.ArrayList;
import java.util.List;


public class SimpleConjunction {

    private List<VariableOrConst> elements;

    public SimpleConjunction(VariableOrConst e) {
        elements = new ArrayList<VariableOrConst>();
        elements.add(e);
    }

    public SimpleConjunction(List<VariableOrConst> e) {
        elements = new ArrayList<VariableOrConst>();
        elements.addAll(e);
    }

    public SimpleConjunction(VariableOrConst a, VariableOrConst b) {
        elements = new ArrayList<VariableOrConst>();
        elements.add(a);
        elements.add(b);
    }

    public SimpleConjunction(SimpleConjunction a, SimpleConjunction b) {
        elements = new ArrayList<VariableOrConst>();
        elements.addAll(a.items());
        elements.addAll(b.items());
    }

    public List<VariableOrConst> items() {
        return new ArrayList(elements);
    }

    public SimpleConjunction optimize() {
        List<VariableOrConst> r = new ArrayList();
        int c = 0xFFFFFFFF;
        for (VariableOrConst i : elements) {
            if (i instanceof Const) {
                c = c & ((Const)i).getValue();
            } else {
                boolean found = false;
                for (VariableOrConst v : r) {
                    if (v.equals(i)) {
                        found = true;
                    }
                    if (v.equals(i.invert()))
                        return new SimpleConjunction(new Const(0));
                }
                if (!found)
                    r.add(i);
            }
        }
        if (c == 0) {
            return new SimpleConjunction(new Const(0));
        }
        if (c != 0xFFFFFFFF) {
            r.add(new Const(c));
        }
        return new SimpleConjunction(r);
    }

    public String toString() {
        String r = "";
        for (VariableOrConst e : elements) {
            if (r.isEmpty()) {
                r = r + e.toString();
            } else {
                r = r + " & " + e.toString();
            }
        }
        return r;
    }

}
