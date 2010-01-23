package mds1;

public class Not1 implements IExp1 {

    private final IExp1 exp;

    public Not1(IExp1 exp) {
        this.exp = exp;
    }

    public IExp1 getExp() {
        return exp;
    }

    public IExp1 and(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.and(this);
        if (exp instanceof Not1)
            return this.exp.and(exp.not()).not();
        if (exp instanceof Var1)
            return exp.and(this);
        return new And1(this, exp);
    }

    public IExp1 or(IExp1 exp) {
        if (exp instanceof Const1)
            return exp.or(this);
        return new Or1(this, exp);
    }

    public IExp1 xor(IExp1 exp) {
        return exp.not().and(this).or(this.not().and(exp));
//        if (exp instanceof Const1)
  //          return exp.xor(this);
    //    if (exp instanceof Not1)
      //      return new Xor1(this.exp, ((Not1)exp).exp);
        //return new Xor1(this, exp);
    }

    public IExp1 not() {
        return exp;
    }

    public IExp1 substitute(Var1 v, Const1 c) {
        return exp.substitute(v, c).not();
    }

    @Override
    public String toString() {
        return "!" + exp;
    }

    public IExp1 optimize() {
        return exp.not();
    }

    public PDNF toPDNF() {
        throw new UnsupportedOperationException();
    }

}
