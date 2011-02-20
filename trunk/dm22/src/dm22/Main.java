package dm22;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void print(int[][] a) {
        int w = a.length;
        System.out.print("\\begin{tabular}{|");
        for (int i = 0; i <= w; i++)
            System.out.print("c|");
        System.out.println("}\\hline");
        for (int i = 1; i <= w; i++)
            System.out.print("& " + i);
        System.out.println("\\\\\\hline");
        for (int i = 0; i < a.length; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < a[i].length; j++) {
                System.out.print("&");
                if (a[i][j] >= 9000)
                    System.out.print("$\\infty$");
                else
                    System.out.print(a[i][j]);
            }
            System.out.println("\\\\\\hline");
        }
        System.out.println("\\end{tabular}");
        System.out.println();
    }

    private static int min(int a, int b) {
        if (a > b)
            return b;
        else
            return a;
    }

    private static List<Integer> findMaxFlowPath(int[][] c, int s, int d) {
        boolean[] v = new boolean[c.length];
        int[] f = new int[c.length];
        List<Integer>[] p = new List[c.length];
        for (int i = 0; i < c.length; i++)
            p[i] = new ArrayList();
        f[s] = 9000;
        boolean u = true;
        while (u) {
            u = false;
            for (int i = 0; i < c.length; i++)
                for (int j = 0; j < c.length; j++)
                    if (i != j && min(f[i], c[i][j]) > f[j]) {
                        p[j].clear();
                        p[j].addAll(p[i]);
                        p[j].add(i);
                        f[j] = min(f[i], c[i][j]);
                        u = true;
                    }
        }
        p[d].add(0, f[d]);
        p[d].add(d);
        return p[d];
    }


  public static void main(String[] args) {
        int[][] w = new int[][] {
            {0, 0, 23, 0, 0, 66, 0},
            {56, 0, 0, 54, 0, 0, 34},
            {0, 56, 0, 0, 0, 0, 0},
            {0, 0, 5, 0, 39, 30, 0},
            {34, 0, 0, 0, 0, 0, 46},
            {0, 0, 73, 31, 19, 0, 4 },
            {0, 0, 0, 3, 0, 43, 0}
        };
        w = new int[][] {
            {0, 1, 1, 0},
            {0, 0, 0, 1},
            {0, 1, 0, 1},
            {0, 0, 0, 0}
        };
        List<Integer> F = new ArrayList();
        while (true) {
            List<Integer> p = findMaxFlowPath(w, 0, 3);
            if (p.get(0) == 0)
                break;
            print(w);
            int f = p.remove(0);
            System.out.print("$ ");
            System.out.print((p.get(0) + 1));
            for (int i = 1; i < p.size(); i++) {
                w[p.get(i-1)][p.get(i)] -= f;
                w[p.get(i)][p.get(i-1)] += f;
                System.out.print(" \\to " + (p.get(i) + 1));
            }
            System.out.println(" : " + f + "$");
            System.out.println();
            F.add(f);
        }
        print(w);
        System.out.print("$M_f=" + F.get(0));
        int m = F.get(0);
        for (int i = 1; i < F.size(); i++) {
            System.out.print("+" + F.get(i));
            m += F.get(i);
        }
        System.out.print("=" + m + "$");
/*    int s = 1;
    int t = 2;
    int flow = maxFlow(w, s, t);
    print(7, w);*/
  }

/*    public static void main(String[] args) {
        int[][] w = new int[][] {
            {0, 9000, 23, 9000, 9000, 66, 9000},
            {56, 0, 9000, 54, 9000, 9000, 34},
            {9000, 56, 0, 9000, 9000, 9000, 9000},
            {9000, 9000, 5, 0, 39, 30, 9000},
            {34, 9000, 9000, 9000, 0, 9000, 46},
            {9000, 9000, 73, 31, 19, 0, 4 },
            {9000, 9000, 9000, 3, 9000, 43, 0}
        };
        print(7, w);
        for (int k = 0; k < 7; k++) {
            for (int i = 0; i < 7; i++)
                for (int j = 0; j < 7; j++)
                    w[i][j] = min(w[i][j], w[i][k] + w[k][j]);
            print(7, w);
        }
    }
*/
}
