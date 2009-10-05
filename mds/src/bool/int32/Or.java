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

    public boolean isZero() {
        return a.isZero() && b.isZero();
    }

    public SCNF toSCNF() {
        List<SimpleConjunction> sc = new ArrayList();
        sc.addAll(a.optimize().toSCNF().items());
        sc.addAll(b.optimize().toSCNF().items());
        return new SCNF(sc);
    }

    public Expression rotate(int rotate) {
        return new Or(a.rotate(rotate), b.rotate(rotate));
    }

    public Expression invert() {
        return new And(a.invert(), b.invert());
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " | " + b.toString() + ")";
    }

    public Expression optimize() {
        Expression oa = a.optimize();
        Expression ob = b.optimize();
        if (oa.isZero())
            return ob;
        if (ob.isZero())
            return oa;
        return this;
    }

}
