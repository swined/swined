package fac;

import java.util.ArrayList;
import java.util.List;

public class Const {

    private final int value;
    private final static Const consts[];
    private final static Tuple<Const, Const> multiplications[][];

    static {
        consts = new Const[0x100];
        for (int i = 0; i < 0x100; i++)
            consts[i] = new Const(i);
        multiplications = new Tuple[0x100][0x100];
        for (int i = 0; i < 0x100; i++)
            for (int j = 0; j < 0x100; j++)
                multiplications[i][j] = multiply(create(i), create(j));

    }

    private Const(int value) {
        this.value = value & 0xFF;
    }

    public static Const create(int value) {
        return consts[value & 0xFF];
    }

    public static Tuple<Const, Const> multiply(Const a, Const b) {
        int r = a.value * b.value;
        return new Tuple(create(r), create(r >> 8));
    }

    public Tuple<Const, Const> multiply(Const c) {
        return multiplications[this.value][c.value];
    }

    public boolean isZero() {
        return value == 0;
    }

    public boolean isOne() {
        return value == 1;
    }

    // solve c + k * x + l * y = this
    public List<Tuple<Const, Const>> solveLinear(Const c, Const k, Const l) {
        List<Tuple<Const, Const>> r = new ArrayList();
        for (int i = 0; i < 0x100; i++)
            for (int j = i; j < 0x100; j++)
                if (((c.value + i * k.value + j * l.value) & 0xFF) == value)
                    r.add(new Tuple(new Const(i), new Const(j)));
        return r;

    }

    public List<Tuple<Const, Const>> demultiplyMod10() {
        List<Tuple<Const, Const>> r = new ArrayList();
        for (int i = 0; i < 0x100; i++)
            for (int j = i; j < 0x100; j++)
                if (((i * j) & 0xFF) == value)
                    r.add(new Tuple(new Const(i), new Const(j)));
        return r;
    }

    @Override
    public String toString() {
        return Integer.toHexString(value & 0xFF);
    }

}
