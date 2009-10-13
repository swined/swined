package bool.int32.v1;

import java.util.ArrayList;
import java.util.List;

public class Variable {

    private final String name;
    private final boolean negative;
    private final int rotate;
    private Variable invert;
    private static List<Variable> pool = new ArrayList();

    public static Variable create(String name) {
        return create(name, false, 0, null);
    }

    public static Variable create(String name, boolean negative, int rotate) {
        return create(name, negative, rotate, null);
    }

    public static Variable create(String name, boolean negative, int rotate, Variable invert) {
        rotate = rotate % 32;
        for (int i = 0; i < pool.size(); i++) {
            Variable v = pool.get(i);
            if (v.name.equals(name) && v.negative == negative && v.rotate == rotate)
                return v;
        }
        Variable v = new Variable(name, negative, rotate, invert);
        pool.add(v);
        return v;
    }

    public boolean isFalse() {
        return false;
    }

    public boolean isTrue() {
        return false;
    }

    private Variable(String name, boolean negative, int rotate, Variable invert) {
        this.name = name;
        this.negative = negative;
        this.rotate = rotate;
        this.invert = invert;
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
        if (invert == null)
            invert = Variable.create(name, !negative, rotate, this);
        return invert;
    }

    @Override
    public String toString() {
        return (negative ? "!" : "") + name + (rotate == 0 ? "" : "<<" + Integer.toString(rotate));
    }

}
