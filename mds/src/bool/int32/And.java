package bool.int32;

public class And implements Expression {

    private final Expression a;
    private final Expression b;
    private Expression invert;

    public And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public And(Expression a, Expression b, Expression invert) {
        this.a = a;
        this.b = b;
        this.invert = invert;
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
        if (invert == null)
            invert = new Or(a.invert(), b.invert(), this);
        return invert;
    }

    private static Expression optimize(Expression a, Expression b) {
        if (a.equals(b))
            return a;
        if (a instanceof Const) {
            Const c = (Const)a;
            if (a.isFalse())
                return Const.FALSE();
            if (c.isTrue())
                return b;
            if (b instanceof Variable)
                return new SimpleConjunction(c, (Variable)b);
        }
        if (a instanceof Variable) {
            Variable v = (Variable)a;
            if (b instanceof Variable)
                return new SimpleConjunction(new SimpleConjunction(v), new SimpleConjunction((Variable)b));
            if (b instanceof SimpleConjunction)
                return new SimpleConjunction(new SimpleConjunction(v), (SimpleConjunction)b);
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
