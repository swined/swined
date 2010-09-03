package dm25;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static int[] a(int[][] l) {
        int[] a = new int[l.length];
        for (int i = 0; i < l.length; i++)
            a[i] = 9000;
        for (int i = 0; i < l.length; i++)
            for (int j = 0; j < l.length; j++)
                if (a[i] > l[i][j])
                    a[i] = l[i][j];
        return a;
    }

    private static int[] b(int[][] l) {
        int[] a = a(l);
        int[] b = new int[l.length];
        for (int i = 0; i < l.length; i++)
            b[i] = 9000;
        for (int i = 0; i < l.length; i++)
            for (int j = 0; j < l.length; j++)
                if (b[j] > l[i][j] - a[i])
                    b[j] = l[i][j] - a[i];
        return b;
    }

    private static int c(int[][] l) {
        int c = 0;
        int[] a = a(l);
        int[] b = b(l);
        for (int i = 0; i < l.length; i++)
            c += a[i] + b[i];
        return c;
    }

    private static void print(int[][] l) {
        int w = l.length;
        int[] a = a(l);
        int[] b = b(l);
        System.out.print("\\begin{tabular}{|");
        for (int i = 0; i < w + 2; i++)
            System.out.print("c|");
        System.out.println("}\\hline");
        for (int i = 1; i <= w; i++)
            System.out.print("& " + i);
        System.out.print("& $\\alpha$");
        System.out.println("\\\\\\hline");
        for (int i = 0; i < l.length; i++) {
            System.out.print(i + 1);
            for (int j = 0; j < l[i].length; j++) {
                System.out.print("& ");
                if (l[i][j] < 9000)
                    System.out.print(l[i][j]);
            }
            System.out.print("& " + a[i]);
            System.out.println("\\\\\\hline");
        }
        System.out.print("$\\beta$");
        for (int i = 0; i < l.length; i++)
            System.out.print("& " + b[i]);
        int c = c(l);
        System.out.println("& " + c + "\\\\\\hline");
        System.out.println("\\end{tabular}");
        System.out.println();
    }

    private static void print(List<Integer> p) {
        System.out.print("$" + (p.get(0) + 1));
        for (int i = 1; i < p.size(); i++)
            System.out.print(" \\to " + (p.get(i) + 1));
        System.out.print("$");
    }

    private static int l(int[][] w, List<Integer> u) {
        int l = 0;
        for (int i = 1; i < u.size(); i++)
                l += w[u.get(i - 1)][u.get(i)];
        return l;
    }

    private static List<Integer> h(int[][] w, List<Integer> p) {
        List<Integer> u = new ArrayList();
        u.addAll(p);
        while (true) {
            int c = 0;
            while (u.contains(c))
                c++;
            if (c >= w.length)
                break;
            for (int i = 0; i < w.length; i++)
                if (!u.contains(i)) {
                    //System.out.println(i + " / " + w[u.get(u.size()-1)][i] + " / " + w[u.get(u.size()-1)][c]);
                    if (w[u.get(u.size()-1)][i] < w[u.get(u.size()-1)][c])
                        c = i;
                }
            if (!u.contains(c))
                u.add(c);
            else 
                break;
        }
        u.add(u.get(0));
        return u;
    }

    private static boolean solve(int[][] w, int rec, List<Integer> rc, List<Integer> path) {
        if (path.size() < 2) {
            for (int i = 0; i < w.length; i++) {
                if (path.contains(i))
                    continue;
                List<Integer> np = new ArrayList();
                np.addAll(path);
                np.add(i);
                if (solve(w, rec, rc, np))
                    return true;
            }
        }
        List<Integer> pr = h(w, path);
        System.out.println("проверяем ");
        print(path);
        System.out.println();
        System.out.println();

        if (rec > l(w, pr)) {
            rec = l(w, pr);
            rc = pr;
            print(pr);
            System.out.println(" : $H_{rec}$ = " + l(w, pr));
            System.out.println();
        }
        print(w);
        if (c(w) > rec) {
            System.out.println("$H_{min} > H_{rec} \\Rightarrow $ отбрасываем дерево");
            System.out.println();
            return false;
        }
        if (c(w) == rec) {
            System.out.println("$H_{min} = H_{rec} \\Rightarrow $ решение найдено:");
            System.out.println();
            print(rc);
            return true;
        }
        for (int i = 0; i < w.length; i++)
            if (!path.contains(i)) {
                List<Integer> np = new ArrayList();
                np.addAll(path);
                np.add(i);
                int[][] nw = clone(w);
                cleanup(nw, np);
                if (solve(nw, rec, rc, np))
                    return true;
            }
        return false;
    }

    private static void cleanup(int[][] w, List<Integer> p) {
        for (int i = 1; i < p.size(); i++) {
            int prev = p.get(i - 1);
            int cur = p.get(i);
            for (int j = 0; j < w.length; j++) {
                if (j != cur)
                    w[prev][j] = 9000;
                if (j != prev)
                    w[j][cur] = 9000;
            }
        }
        w[p.get(p.size() - 1)][p.get(0)] = 9000;
    }

    private static int[][] clone(int[][] w) {
        int[][] n = new int[w.length][w.length];
        for (int i = 0; i < w.length; i++)
            for (int j = 0; j < w.length; j++)
                n[i][j] = w[i][j];
        return n;
    }

    public static void main(String[] args) {
        int[][] w = new int[][] {
            { 9000, 36, 44, 37, 41, 53, 43 },
            { 42, 9000, 48, 57, 43, 45, 51 },
            { 35, 41, 9000, 39, 39, 42, 38 },
            { 38, 34, 35, 9000, 52, 51, 34 },
            { 40, 43, 47, 37, 9000, 49, 35 },
            { 33, 40, 56, 53, 36, 9000, 45 },
            { 33, 39, 39, 36, 34, 48, 9000 }
        };
        print(w);
        List<Integer> r = new ArrayList();
        r.add(6);
        r = h(w, r);
        solve(w, l(w, r), r, new ArrayList());
    }

}
