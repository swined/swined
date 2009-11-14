package fac;

import java.util.ArrayList;
import java.util.List;

public class ConstExpression {

    private final List<Tuple<Integer, Const>> muls;

    public ConstExpression(List<Tuple<Integer, Const>> muls) {
        this.muls = new ArrayList(muls);
        if (this.muls.isEmpty())
            this.muls.add(new Tuple(0, new Const(0)));
    }

    public static ConstExpression constExpression(int c[]) {
        List<Tuple<Integer, Const>> muls = new ArrayList();
        for (int i = 0; i < c.length; i++)
            muls.add(new Tuple<Integer, Const>(i, new Const(c[i])));
        return new ConstExpression(muls);
    }

    public Const mod10() {
        Const r = null;
        for (Tuple<Integer, Const> m : muls)
            if (m.getX() == 0) {
                if (r == null)
                        r = m.getY();
                else
                    r = r.multiply(m.getY()).getX();
            }
        if (r == null)
            return new Const(0);
        else
            return r;
    }

    public ConstExpression div10() {
        List<Tuple<Integer, Const>> r = new ArrayList();
        for (Tuple<Integer, Const> m : muls)
            if (m.getX() > 0)
                r.add(new Tuple(m.getX() - 1, m.getY()));
        return new ConstExpression(r);
    }

    public boolean isZero() {
        if (muls.size() == 1)
            return muls.get(0).getY().isZero();
        return false;
    }

    @Override
    public String toString() {
        String r = "";
        for (Tuple<Integer, Const> m : muls) {
            if (!r.isEmpty())
                r += " + ";
            r += m;
        }
        return r;
    }

}
