package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class Or implements Expression {

    private Expression a;
    private Expression b;

    public Or(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public SCNF toSCNF() {
        List<SimpleConjunction> sc = new ArrayList();
        sc.addAll(a.toSCNF().items());
        sc.addAll(b.toSCNF().items());
        return new SCNF(sc);
    }

    public Expression rotate(int rotate) {
        return new Or(a.rotate(rotate), b.rotate(rotate));
    }

    public Expression optimize() {
        Expression oa = a.optimize();
        Expression ob = b.optimize();
        if (oa instanceof Const && ob instanceof Const) {
            return new Const(((Const)oa).getValue() | ((Const)ob).getValue());
        }
        return new Or(oa, ob);
    }

    public Expression invert() {
        return new And(a.invert(), b.invert());
    }

    public String toString() {
        return "(" + a.toString() + " | " + b.toString() + ")";
    }

}
