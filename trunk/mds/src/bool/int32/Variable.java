package bool.int32;

public class Variable implements Expression {

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

    public String getName() {
        return name;
    }

    public boolean isNegative() {
        return negative;
    }

    public int getRotate() {
        return rotate;
    }

    public SCNF toSCNF() {
        return new SCNF(new SimpleConjunction(this));
    }

    public Variable rotate(int rotate) {
        return new Variable(name, negative, this.rotate + rotate);
    }

    public Variable invert() {
        return new Variable(name, !negative, rotate);
    }

    @Override
    public String toString() {
        return (negative ? "!" : "") + name + (rotate == 0 ? "" : "<<" + Integer.toHexString(rotate));
    }

    @Override
    public int hashCode() {
        int code = rotate + (negative ? 1 : 0);
        for (char c : name.toCharArray()) {
            code += c;
        }
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Variable) {
            Variable v = (Variable)o;
            if (!name.equals(v.getName()))
                return false;
            if (negative != v.isNegative())
                return false;
            return rotate == v.getRotate();
        } else {
            return false;
        }
    }

}
