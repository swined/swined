package fac;

import java.util.ArrayList;
import java.util.List;

public class Mod10Expression {

    private final List<Multiplication> muls;

    public Mod10Expression(List<Multiplication> muls) {
        this.muls = new ArrayList(muls);
        if (this.muls.isEmpty())
            this.muls.add(new Multiplication(Const.create(0)));
    }

    public List<Multiplication> getMultiplications() {
        return new ArrayList(muls);
    }

    @Override
    public String toString() {
        String r = "";
        for (Multiplication m : muls) {
            if (!r.isEmpty())
                r += " + ";
            r += m;
        }
        return r;
    }

}
