package bool.int32;

public class And implements Expression {

    private Expression a;
    private Expression b;

    public And(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public Expression invert() {
        return new Or(a.invert(), b.invert());
    }

    public String toString() {
        return a.toString() + " & " + b.toString();
    }

}
