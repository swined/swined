package bool.int32;

public class Not implements Expression {

    private Expression expr;

    public Not(Expression expr) {
        this.expr = expr;
    }

    public boolean isZero() {
        return !expr.isZero();
    }

    public SCNF toSCNF() {
        return expr.invert().toSCNF();
    }

    public Expression rotate(int s) {
        return new Not(expr.rotate(s));
    }

    public Expression invert() {
        return expr;
    }

    public String toString() {
        return "!(" + expr.toString() + ")";
    }

}
