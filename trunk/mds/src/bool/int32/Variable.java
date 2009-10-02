package bool.int32;

public class Variable implements Expression {

    private String name;
    private boolean negative;
    private int shift;

    public Variable(String name, boolean negative, int shift) {
        this.name = name;
        this.negative = negative;
        this.shift = shift;
    }

    public String toString() {
        String basic = (negative ? "!" : "") + name;
        if (shift > 0)
            return basic + "<<" + Integer.toHexString(shift);
        if (shift < 0)
            return basic + ">>" + Integer.toHexString(shift);
        return basic;
    }

}
