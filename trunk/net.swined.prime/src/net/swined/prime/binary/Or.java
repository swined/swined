package net.swined.prime.binary;


public class Or extends Expression {

    public final IExpression a;
    public final IExpression b;

    public Or(IExpression a, IExpression b) {
        super(a, b);
        this.a = a;
        this.b = b;
    }

    @Override
    protected IExpression notImpl() {
        return a.not().and(b.not());
    }

    @Override
    public String toString() {
        return "(" + a + " | " + b + ")";
    }

}
