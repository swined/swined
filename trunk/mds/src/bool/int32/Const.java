package bool.int32;

import java.util.ArrayList;
import java.util.List;

public class Const implements Expression {

    private final int value;
    private static List<Const> pool = new ArrayList();

    public static Const create(int value) {
        for (Const c : pool) {
            if (c.getValue() == value)
                return c;
        }
        Const c = new Const(value);
        pool.add(c);
        return c;
    }

    private Const(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean isFalse() {
        return value == 0;
    }

    public boolean isTrue() {
        return value == 0xFFFFFFFF;
    }

    public Const invert() {
        return Const.create(value ^ 0xFFFFFFFF);
    }

    public Const rotate(int rotate) {
        return Const.create(value << rotate | value >> (32 - rotate));
    }

    @Override
    public String toString() {
        return Integer.toHexString(value);
    }

    public Expression optimize() {
        return this;
    }

}
