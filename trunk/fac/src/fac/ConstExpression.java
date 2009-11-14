package fac;

import java.util.ArrayList;
import java.util.List;

public class ConstExpression {

    private final List<Tuple<Integer, Const>> muls;

    public ConstExpression(List<Tuple<Integer, Const>> muls) {
        this.muls = new ArrayList(muls);
    }

    public static ConstExpression constExpression(int c[]) {
        List<Tuple<Integer, Const>> muls = new ArrayList();
        for (int i = 0; i < c.length; i++)
            muls.add(new Tuple<Integer, Const>(i, new Const(c[i])));
        return new ConstExpression(muls);
    }

    public Const mod10() {
        Const r = new Const(1);
        for (Tuple<Integer, Const> m : muls)
            if (m.getX() == 0)
                r = r.multiply(m.getY()).getX();
        return r;
    }

    @Override
    public String toString() {
        String r = "";
        for (Tuple<Integer, Const> m : muls) {
            if (!r.isEmpty())
                r += " + ";
            if (m.getX() != 0)
                r += "10^" + m.getX() + "*";
            r += m.getY();
        }
        return r;
    }

}
