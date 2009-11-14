package fac;

import java.util.ArrayList;
import java.util.List;

public class Expression {

    private final List<Multiplication> muls;

    public Expression(List<Multiplication> muls) {
        this.muls = new ArrayList(muls);
    }

}
