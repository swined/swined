package bool.int32;

import java.util.BitSet;
import java.util.HashMap;

public class Const implements Expression {

    private final BitSet value;
    private static Const TRUE;
    private static Const FALSE;
    private static HashMap<BitSet, Const> pool = new HashMap();

    public static Const create(BitSet value) {
        if (pool.containsKey(value))
            return pool.get(value);
        Const c = new Const(value);
        pool.put(value, c);
        return c;
    }

    private Const(BitSet value) {
        this.value = (BitSet)value.clone();
    }

    public static Const TRUE() {
        if (TRUE == null) {
            BitSet t = new BitSet();
            for (int i = 0; i < 32; i++)
                t.set(i, true);
            TRUE = Const.create(t);
        }
        return TRUE;
    }

    public static Const FALSE() {
        if (FALSE == null)
            FALSE = Const.create(new BitSet());
        return FALSE;
    }

    public BitSet getValue() {
        return value;
    }

    public boolean isFalse() {
        return value.cardinality() == 0;
    }

    public boolean isTrue() {
        return value.cardinality() == 32;
    }

    public Const invert() {
        BitSet n = (BitSet)value.clone();
        n.xor(TRUE().getValue());
        return Const.create(n);
    }

    public Const rotate(int rotate) {
        while (rotate < 0)
            rotate += 32;
        BitSet r = new BitSet();
        for (int i = 0; i < 32; i++)
            r.set((i + rotate) % 32, value.get(i));
        return Const.create(r);
    }

    public Const shift(int shift) {
        BitSet r = new BitSet();
        for (int i = 0; i < 32; i++)
            r.set(i + shift, value.get(i));
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
        String r = "";
        char c[] = "0123456789abcdef".toCharArray();
        for (int i = 28; i >= 0; i-=4 ) {
            r += c[(value.get(i) ? 1 : 0) + 2 * (value.get(i + 1) ? 1 : 0) + 4 * (value.get(i + 2) ? 1 : 0) + 8 * (value.get(i + 3) ? 1 : 0)];
        }
        return r;
    }

    public Expression optimize() {
        return this;
    }

}
