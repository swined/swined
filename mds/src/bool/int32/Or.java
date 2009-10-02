package bool.int32;

public class Or implements Expression {

    private Expression a;
    private Expression b;

    public Or(Expression a, Expression b) {
        this.a = a;
        this.b = b;
    }

    public Expression invert() {
        return new And(a.invert(), b.invert());
    }

    public String toString() {
        return "(" + a.toString() + " | " + b.toString() + ")";
    }

}
