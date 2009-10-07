package bool.int32;

public class And implements Expression {

    private final Expression a;
    private final Expression b;

    public And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

    public boolean isFalse() {
        return a.isFalse() || b.isFalse();
    }

    public boolean isTrue() {
        return a.isTrue() && b.isTrue();
    }

    public Expression rotate(int s) {
        return new And(a.rotate(s), b.rotate(s));
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " & " + b.toString() + ")";
    }

    public Expression invert() {
        return new Or(a.invert(), b.invert());
    }

    public Expression optimize() {
        final Expression oa = a.optimize();
        final Expression ob = b.optimize();
        return new And(oa, ob);
    }

}
