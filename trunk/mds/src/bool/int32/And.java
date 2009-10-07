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

    private static Expression optimize(Expression a, Expression b) {
        if (a.equals(b))
            return a;
        if (a instanceof Const) {
            Const c = (Const)a;
            if (a.isFalse())
                return Const.FALSE();
            if (c.equals(Const.TRUE()))
                return b;
            if (b instanceof Variable)
                return new SimpleConjunction(new SimpleConjunction(c), new SimpleConjunction((Variable)b));
        }
        if (a instanceof SimpleConjunction) {
            SimpleConjunction sc = (SimpleConjunction)a;
            if (b instanceof SimpleConjunction)
                return new SimpleConjunction(sc, (SimpleConjunction)b);
        }
        return null;
    }

    public Expression optimize() {
        final Expression oa = a.optimize();
        final Expression ob = b.optimize();
        Expression real = optimize(oa, ob);
        if (real == null)
            real = optimize(ob, oa);
        if (real == null) {
            return new And(oa, ob);
        } else {
            return real.optimize();
        }
    }

}
