package mds1;

public class Exp32 {

    private final IExp1[] bits;

    public Exp32(IExp1[] bits) {
        this.bits = bits;
    }

    public Exp32(long value) {
        bits = new IExp1[32];
        for (int i = 0; i < 32; i++)
            bits[i] = Const1.create(1 == ((value >> i) & 1));
    }

    public Exp32(String name) {
        bits = new IExp1[32];
        for (int i = 0; i < 32; i++)
            bits[i] = new Var1(name + "[" + i + "]");
    }

    public IExp1[] bits() {
        return bits;
    }

    public Exp32 and(Exp32 exp) {
        IExp1[] r = new IExp1[32];
        for (int i = 0; i < 32; i++)
            r[i] = new And1(bits[i], exp.bits[i]);
        return new Exp32(r);
    }

    public Exp32 or(Exp32 exp) {
        IExp1[] r = new IExp1[32];
        for (int i = 0; i < 32; i++)
            r[i] = new Or1(bits[i], exp.bits[i]);
        return new Exp32(r);
    }

    public Exp32 xor(Exp32 exp) {
        IExp1[] r = new IExp1[32];
        for (int i = 0; i < 32; i++) {
            And1 a = new And1(bits[i], new Not1(exp.bits[i]));
            And1 b = new And1(new Not1(bits[i]), exp.bits[i]);
            r[i] = new Or1(a, b);
        }
        return new Exp32(r);
    }

    public Exp32 not() {
        IExp1[] r = new IExp1[32];
        for (int i = 0; i < 32; i++)
            r[i] = new Not1(bits[i]);
        return new Exp32(r);
    }

    public Exp32 shift(int d) {
        IExp1[] r = new IExp1[32];
        for (int i = 0; i < 32; i++)
            r[(i + d) % 32] = bits[i];
        return new Exp32(r);
    }

    public Exp32 sum(Exp32 exp) {
        IExp1[] q = new IExp1[32];
        q[0] = Const1.create(false);
        for (int i = 0; i < 31; i++)
            q[i + 1] = new Or1(new And1(bits[i], exp.bits[i]), new And1(q[i], new Or1(bits[i], exp.bits[i])));
        return this.xor(exp).xor(new Exp32(q));
    }

    public Long asLong() {
        long r = 0;
        for (int i = 0; i < 32; i++)
            if (bits[i] instanceof Const1) {
                Const1 bit = (Const1)bits[i];
                r = r | ((bit.getValue() ? 1 : 0) << i);
            } else {
                return null;
            }
        return r & 0xFFFFFFFFL;
    }

    public IExp1 equation() {
        IExp1 r = Const1.create(false);
        for (int i = 0; i < 32; i++)
            r = new Or1(r, bits[i]);
        return r;
    }

    @Override
    public String toString() {
        Long l = asLong();
        if (l != null)
            return "0x" + Long.toHexString(l);
        String r = "<Exp32>\n";
        for (int i = 0; i < 32; i++)
            r += "" + i + " = " + bits[i] + "\n";
        r += "</Exp32>";
        return r;
    }

}
