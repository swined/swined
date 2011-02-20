package dynp14;

public class Main {

    public static void main(String[] args) {
        Num[][] P = new Num[5][9];
        int[][] T = new int[5][9];
        for (int i = 0; i < 9; i++) {
            P[0][i] = new Num(1);
            T[0][i] = 0;
            System.out.println(" & " + i);
        }
        System.out.println("\\\\\\hline");
        for (int k = 1; k < 5; k++) {
            System.out.println("$P_{" + k + "}$");
            for (int y = 0; y < 9; y++) {
                Num v = new Num(0);
                int m = 0;
                for (int t = 0; t <= y; t++) {
                    Num nv = P[k-1][y-t].mul(new Num(1).sub(new Num(1, k+1).pow(t)));
                    if (nv.gt(v)) {
                        v = nv;
                        m = t;
                    }
                }
                P[k][y] = v;
                T[k][y] = m;
                System.out.println(" & " + v + " / " + m);
            }
            System.out.println("\\\\\\hline");
        }
    }

}
