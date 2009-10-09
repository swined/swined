package bool.int32;

public class SimpleConjunction implements Expression {

    private Const coef;
    private Variable vars[];
    private Boolean isFalse;

    public SimpleConjunction rotate(int s) {
        Const rc = coef.rotate(s);
        Variable v[] = new Variable[vars.length];
        for (int i = 0; i < vars.length; i++)
            v[i] = vars[i].rotate(s);
        return new SimpleConjunction(rc, v);
    }

    public SimpleConjunction(Const c) {
        coef = c;
        vars = new Variable[0];
        checkConst();
    }

    public SimpleConjunction(Variable e) {
        coef = Const.TRUE();
        vars = new Variable[1];
        vars[0] = e;
        checkConst();
    }

    public SimpleConjunction(Const c, Variable e) {
        coef = c;
        vars = new Variable[1];
        vars[0] = e;
        checkConst();
    }

    public SimpleConjunction(Const c, Variable[] e) {
        coef = c;
        vars = e;
        checkConst();
    }

    public SimpleConjunction(SimpleConjunction a, SimpleConjunction b) {
        coef = a.getCoef().and(b.getCoef());
        Variable va[] = a.getVars();
        Variable vb[] = b.getVars();
        vars = new Variable[va.length + vb.length];
        for (int i = 0; i < va.length; i++)
            vars[i] = va[i];
        for (int i = 0; i < vb.length; i++)
            vars[i + va.length] = vb[i];
        checkConst();
    }

    public Const getCoef() {
        return coef;
    }

    public Variable[] getVars() {
        return vars;
    }

    public void checkConst() {
        if (isFalse()) {
            coef = Const.FALSE();
            vars = new Variable[0];
        }
        if (isTrue()) {
            coef = Const.TRUE();
            vars = new Variable[0];
        }
    }

    public boolean isFalse() {
        if (isFalse != null)
            return isFalse;
        if (coef.isFalse())
            return true;
        for (Variable v1 : vars)
            for (Variable v2 : vars)
                if (v1.invert().equals(v2)) {
                    isFalse = true;
                    return true;
                }
        isFalse = false;
        return false;
    }

    public boolean isTrue() {
        return vars.length == 0 && coef.isTrue();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[");
        b.append(coef);
        for (Variable v : vars) {
            b.append(" & ");
            b.append(v);
        }
        b.append("]");
        return b.toString();
    }

    public SimpleDisjunction invert() {
        Variable v[] = new Variable[vars.length];
        for (int i = 0; i < v.length; i++)
            v[i] = vars[i].invert();
        return new SimpleDisjunction(coef.invert(), v);
    }

    public Expression optimize() {
        if (isFalse())
            return Const.FALSE();
        return this;
    }

}
