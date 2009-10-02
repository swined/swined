package bool.int32;

import java.util.ArrayList;
import java.util.List;


public class SimpleConjunction {

    private List<VariableOrConst> elements;

    public SimpleConjunction(VariableOrConst e) {
        elements = new ArrayList<VariableOrConst>();
        elements.add(e);
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
