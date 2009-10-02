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
        for (SimpleConjunction ca : a.toSCNF().items()) {
            for (SimpleConjunction cb : b.toSCNF().items()) {
                r.add(new SimpleConjunction(ca, cb));
            }
        }
        return new SCNF(r);
    }

    public Expression rotate(int rotate) {
        return new And(a.rotate(rotate), b.rotate(rotate));
    }

    public Expression invert() {
        return new Or(a.invert(), b.invert());
    }

    public Expression optimize() {
        Expression oa = a.optimize();
        Expression ob = b.optimize();
        if (oa instanceof Const && ob instanceof Const) {
            return new Const(((Const)oa).getValue() & ((Const)ob).getValue());
        }
        return new And(oa, ob);
    }

    public String toString() {
        return a.toString() + " & " + b.toString();
    }

}
