package bool.int32;

public class Or implements Expression {

    private final Expression a;
    private final Expression b;
    private Expression invert = null;

    public Or(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public Or(Expression a, Expression b, Expression invert) {
        this.a = a;
        this.b = b;
        this.invert = invert;
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
        if (invert == null)
            invert = new And(a.invert(), b.invert(), this);
        return invert;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + " | " + b.toString() + ")";
    }

    public Expression getA() {
        return a;
    }

    public Expression getB() {
        return b;
    }

    private static Expression optimize(Expression a, Expression b) {
        if (a.equals(b))
            return a;
        if (a instanceof Const) {
            Const c = (Const)a;
            if (c.isTrue())
                return Const.TRUE();
            if (c.isFalse())
                return b;
            if (b instanceof Variable)
                return new SimpleDisjunction(c, (Variable)b);
            if (b instanceof SimpleDisjunction)
                return new SimpleDisjunction(new SimpleDisjunction(c), (SimpleDisjunction)b);
        }
        if (a instanceof Variable) {
            Variable v = (Variable)a;
            if (b instanceof Variable)
                return new SimpleDisjunction(v, (Variable)b);
            if (b instanceof SimpleDisjunction)
                return new SimpleDisjunction(new SimpleDisjunction(v), (SimpleDisjunction)b);
        }
        if (a instanceof SimpleDisjunction) {
            SimpleDisjunction sd = (SimpleDisjunction)a;
            if (b instanceof SimpleDisjunction)
                return new SimpleDisjunction(sd, (SimpleDisjunction)b);
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
            return new Or(oa, ob);
        } else {
            return real.optimize();
        }
    }

}
