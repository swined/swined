package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class Variable implements Expression {

    private final String name;
    private final boolean negative;
    private final int rotate;
    private static List<Variable> pool = new ArrayList();

    public static Variable create(String name) {
        return create(name, false, 0);
    }

    public static Variable create(String name, boolean negative, int rotate) {
        for (Variable v : pool) {
            if (v.name.equals(name) && v.negative == negative && v.rotate == rotate)
                return v;
        }
        Variable v = new Variable(name, negative, rotate);
        pool.add(v);
        return v;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    private Variable(String name, boolean negative, int rotate) {
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

    public Variable rotate(int rotate) {
        return Variable.create(name, negative, this.rotate + rotate);
    }

    public Variable invert() {
        return Variable.create(name, !negative, rotate);
    }

    @Override
    public String toString() {
        return (negative ? "!" : "") + name + (rotate == 0 ? "" : "<<" + Integer.toHexString(rotate));
    }

    public Expression optimize() {
        return this;
    }

}
