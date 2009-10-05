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

    public boolean isFalse() {
        return a.isFalse() && b.isFalse();
    }

    public boolean isTrue() {
        return a.isTrue() || b.isTrue();
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

    public Expression sum(Expression e) {
        return new Or(this, e);
    }

    public Expression optimize() {
        Expression oa = a.optimize();
        Expression ob = b.optimize();
        if (oa.isFalse())
            return ob;
        if (ob.isFalse())
            return oa;
        if (oa instanceof Const && ob instanceof Const)
            return ((Const)oa).or((Const)ob);
        return new Or(oa, ob);
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

}
