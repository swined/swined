package bool.int32;

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
                return new SimpleDisjunction(new SimpleDisjunction(c), new SimpleDisjunction((Variable)b));
            if (b instanceof SimpleDisjunction)
                return new SimpleDisjunction(new SimpleDisjunction(c), (SimpleDisjunction)b);
        }
        if (a instanceof Variable) {
            Variable v = (Variable)a;
            if (b instanceof Variable)
                return new SimpleDisjunction(new SimpleDisjunction(v), new SimpleDisjunction((Variable)b));
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
