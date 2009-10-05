package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class And implements Expression {

    private Expression a;
    private Expression b;

    public And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public SCNF toSCNF() {
        List<SimpleConjunction> r = new ArrayList();
        for (SimpleConjunction ca : a.toSCNF().optimize().items()) {
            for (SimpleConjunction cb : b.toSCNF().optimize().items()) {
                r.add(new SimpleConjunction(ca, cb));
            }
        }
        return new SCNF(r);
    }

    public SCNF rotate(int rotate) {
        return new And(a.rotate(rotate), b.rotate(rotate)).toSCNF();
    }

    public Expression invert() {
        return new Or(new Not(a), new Not(b));
    }

    public String toString() {
        return a.toString() + " & " + b.toString();
    }

}
