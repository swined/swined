package bool.int32;

public class Variable implements Expression, VariableOrConst {

    private String name;
    private boolean negative;
    private int rotate;

    public Variable(String name) {
        this.name = name;
        this.negative = false;
        this.rotate = 0;
    }

    public Variable(String name, boolean negative, int rotate) {
        this.name = name;
        this.negative = negative;
        this.rotate = rotate;
    }

    public SCNF toSCNF() {
        SimpleConjunction sc = new SimpleConjunction(this);
        return new SCNF(sc);
    }

    public Expression rotate(int rotate) {
        return new Variable(name, negative, this.rotate + rotate);
    }

    public Expression optimize() {
        return this;
    }

    public Expression invert() {
        return new Variable(name, !negative, rotate);
    }

    public String toString() {
        return (negative ? "!" : "") + name + (rotate == 0 ? "" : "<<" + Integer.toHexString(rotate));
    }

}
