package bool.int32;

public class And implements Expression {

    private final Expression a;
    private final Expression b;

    public And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public SCNF toSCNF() {
        return a.toSCNF().multiply(b);
    }

    public boolean isZero() {
        return a.isZero() || b.isZero();
    }

    public SCNF rotate(int rotate) {
        return new And(a.rotate(rotate), b.rotate(rotate)).toSCNF();
    }

    public Expression invert() {
        return new Or(a.invert(), b.invert());
    }

    @Override
    public String toString() {
        return a.toString() + " & " + b.toString();
    }

    public Expression optimize() {
        Expression oa = a.optimize();
        Expression ob = b.optimize();
        if (oa.isZero())
            return new Const(0);
        if (ob.isZero())
            return new Const(0);
        return this;
    }

}
