package bool.int32;

public class Not {

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
        return expr.rotate(s).invert();
    }

    public Expression invert() {
        return expr;
    }

    public String toString() {
        return "!(" + expr.toString() + ")";
    }

}
