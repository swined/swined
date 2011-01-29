package net.swined.prime.binary;


public class And extends Expression {

    public final IExpression a;
    public final IExpression b;

    public And(IExpression a, IExpression b) {
        super(a, b);
        this.a = a;
        this.b = b;
    }

    @Override
    protected IExpression notImpl() {
    	return a.not().or(b.not());
    }

    @Override
    public String toString() {
        return "(" + a + " & " + b + ")";
    }
}
