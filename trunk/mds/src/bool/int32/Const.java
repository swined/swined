package bool.int32;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Const implements Expression {

    private final BitSet value;
    private static BitSet defaultMask;
    private static List<Const> pool = new ArrayList();

    public static Const create(BitSet value) {
        for (Const c : pool) {
            if (c.getValue().equals(value))
                return c;
        }
        Const c = new Const(value);
        pool.add(c);
        return c;
    }

    private Const(BitSet value) {
        this.value = (BitSet)value.clone();
        value.and(xFFFFFFFF());
    }

    public static BitSet xFFFFFFFF() {
        if (defaultMask == null) {
            defaultMask = new BitSet();
            for (int i = 0; i < 32; i++)
                defaultMask.set(i, true);
        }
        return defaultMask;
    }

    public BitSet getValue() {
        return value;
    }

    public boolean isFalse() {
        return value.cardinality() == 0;
    }

    public boolean isTrue() {
        return false;
    }

    public Const invert() {
        BitSet n = (BitSet)value.clone();
        n.xor(xFFFFFFFF());
        return Const.create(n);
    }

    public Const rotate(int rotate) {
        BitSet r = new BitSet();
        for (int i = 0; i < 32; i++)
            r.set(i, value.get((i + rotate) % 32));
        return Const.create(r);
    }

    public Const shift(int shift) {
        BitSet r = new BitSet();
        for (int i = 0; i < 32; i++)
            r.set(i, value.get(i + shift));
        return Const.create(r);
    }

    public Const or(Const c) {
        BitSet r = (BitSet)value.clone();
        r.or(c.getValue());
        return Const.create(r);
    }

    public Const and(Const c) {
        BitSet r = (BitSet)value.clone();
        r.and(c.getValue());
        return Const.create(r);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Expression optimize() {
        return this;
    }

}
