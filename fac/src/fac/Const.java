package fac;

import java.util.ArrayList;
import java.util.List;

public class Const {

    private final int value;

    public Const(int value) {
        this.value = value & 0xFF;
    }

    public Tuple<Const, Const> multiply(Const c) {
        int r = value * c.value;
        return new Tuple(new Const(r), new Const(r >> 8));
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isOne() {
        return value == 1;
    }

    public List<Tuple<Const, Const>> demultiplyMod10() {
        List<Tuple<Const, Const>> r = new ArrayList();
        for (int i = 0; i < 0x100; i++)
            for (int j = i; j < 0x100; j++)
                if ((i * j) % 0xFF == value)
                    r.add(new Tuple(new Const(i), new Const(j)));
        return r;
    }

    @Override
    public String toString() {
        return Integer.toHexString(value & 0xFF);
    }

}
