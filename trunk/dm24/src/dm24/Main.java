package dm24;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void print(Num[][] a) {
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
                System.out.print("& " + a[i][j]);
            }
            System.out.println("\\\\\\hline");
        }
        System.out.println("\\end{tabular}");
        System.out.println();
    }

    private static Num min(Num a, Num b) {
        if (a.gt(b))
            return b;
        else
            return a;
    }

    private static List findMaxFlowPath(Num[][] c, int s, int d) {
        Num[] f = new Num[c.length];
        List[] p = new List[c.length];
        for (int i = 0; i < c.length; i++) {
            p[i] = new ArrayList();
            f[i] = new Num(0);
        }
        f[s] = new Num(9000);
        boolean u = true;
        while (u) {
            u = false;
            for (int i = 0; i < c.length; i++)
                for (int j = 0; j < c.length; j++)
                    if (i != j && min(f[i], c[i][j]).gt(f[j])) {
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
        Num[][] w = new Num[6][6];
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++)
                if (i == j)
                    w[i][j] = new Num(0);
                else
                    w[i][j] = new Num(1, (i+1)*(6-j));
        List<Num> F = new ArrayList();
        while (true) {
            List p = findMaxFlowPath(w, 0, 5);
            if (p.get(0).equals(new Num(0)))
                break;
            print(w);
            Num f = (Num)p.remove(0);
            System.out.print("$ ");
            System.out.print((Integer)p.get(0) + 1);
            for (int i = 1; i < p.size(); i++) {
                w[(Integer)p.get(i-1)][(Integer)p.get(i)] = w[(Integer)p.get(i-1)][(Integer)p.get(i)].sub(f);
                w[(Integer)p.get(i)][(Integer)p.get(i-1)] = w[(Integer)p.get(i)][(Integer)p.get(i-1)].add(f);
                System.out.print(" \\to " + ((Integer)p.get(i) + 1));
            }
            System.out.println(" : " + f.toStr() + "$");
            System.out.println();
            F.add(f);
        }
        print(w);
        System.out.print("$M_f=" + F.get(0).toStr());
        Num m = F.get(0);
        for (int i = 1; i < F.size(); i++) {
            System.out.print("+" + F.get(i).toStr());
            m = m.add(F.get(i));
        }
        System.out.print("=" + m.toStr() + "$");
  }

}
