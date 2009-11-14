package fac;

public class Mod10Equation {

    private final Mod10Expression left;
    private final Const right;

    public Mod10Equation(Mod10Expression left, Const right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " = " + right;
    }

}
