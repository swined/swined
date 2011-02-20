package dynp14;

public class Num {

    private final int a;
    private final int b;

    public Num(int a) {
        this.a = a;
        this.b = 1;
    }

    public Num(int a, int b) {
        int d = 2;
        while (d <= a && d <= b) {
            if (a % d == 0 && b % d == 0) {
                a = a / d;
                b = b / d;
            } else {
                d++;
            }
        }
        this.a = a;
        this.b = b;

    }

    public Num add(Num n) {
        return new Num(a * n.b + b * n.a, b * n.b);
    }

    public Num sub(Num n) {
        return add(new Num(-n.a, n.b));
    }

    public Num mul(Num n) {
        return new Num(a * n.a, b * n.b);
    }

    public Num div(Num n) {
        return mul(new Num(n.b, n.a));
    }

    public Num pow(int p) {
        Num r = new Num(1);
        for (int i = 0; i < p; i++)
            r = r.mul(this);
        return r;
    }

    public boolean gt(Num n) {
        return a * n.b > b * n.a;
    }

    @Override
    public String toString() {
        if (a == 0 || b == 1)
            return Integer.toString(a);
        return "$\\frac{" + a + "}{" + b + "}$";
    }

}
