package net.swined.voyager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Flat {

	private static final int INF = Integer.MAX_VALUE;
	private final Point[] points;
	
	public Flat(Point[] points) {
		this.points = Arrays.copyOf(points, points.length);
	}
	
	public int[] getPath() {
		int[][] w = map();
        List<Integer> r = new ArrayList<Integer>();
        r.add(0);
        r = h(w, r);
        List<Integer> solution = solve(w, l(w, r), r, new ArrayList<Integer>());
        int[] path = new int[solution.size()];
        for (int i = 0; i < path.length; i++)
        	path[i] = solution.get(i);
		return path;
	}
	
	private int[][] map() {
		int[][] map = new int[points.length][points.length];
		for (int i = 0; i < points.length; i++)
			for (int j = 0; j < points.length; j++)
				if (i == j)
					map[i][j] = INF;
				else
					map[i][j] = points[i].distance(points[j]);
		return map;
	}
	
    private static int[] a(int[][] l) {
    	int[] a = new int[l.length];
        for (int i = 0; i < l.length; i++)
            a[i] = INF;
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
            b[i] = INF;
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

    private static int l(int[][] w, List<Integer> u) {
    	int l = 0;
        for (int i = 1; i < u.size(); i++)
                l += w[u.get(i - 1)][u.get(i)];
        return l;
    }

    private static List<Integer> h(int[][] w, List<Integer> p) {
        List<Integer> u = new ArrayList<Integer>();
        u.addAll(p);
        while (true) {
            int c = 0;
            while (u.contains(c))
                c++;
            if (c >= w.length)
                break;
            for (int i = 0; i < w.length; i++)
                if (!u.contains(i)) {
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

    private static List<Integer> solve(int[][] w, int rec, List<Integer> rc, List<Integer> path) {
        if (path.size() < 2) {
            for (int i = 0; i < w.length; i++) {
                if (path.contains(i))
                    continue;
                List<Integer> np = new ArrayList<Integer>();
                np.addAll(path);
                np.add(i);
                List<Integer> solution = solve(w, rec, rc, np);
				if (solution != null)
                    return solution;
            }
        }
        List<Integer> pr = h(w, path);
        if (rec > l(w, pr)) {
            rec = l(w, pr);
            rc = pr;
        }
        if (c(w) > rec)
            return null;
        if (c(w) == rec)
            return rc;
        for (int i = 0; i < w.length; i++)
            if (!path.contains(i)) {
                List<Integer> np = new ArrayList<Integer>();
                np.addAll(path);
                np.add(i);
                int[][] nw = clone(w);
                cleanup(nw, np);
                List<Integer> solution = solve(nw, rec, rc, np);
				if (solution != null)
                    return solution;
            }
        return null;
    }

    private static void cleanup(int[][] w, List<Integer> p) {
        for (int i = 1; i < p.size(); i++) {
            int prev = p.get(i - 1);
            int cur = p.get(i);
            for (int j = 0; j < w.length; j++) {
                if (j != cur)
                    w[prev][j] = INF;
                if (j != prev)
                    w[j][cur] = INF;
            }
        }
        w[p.get(p.size() - 1)][p.get(0)] = INF;
    }

    private static int[][] clone(int[][] w) {
    	int[][] n = new int[w.length][w.length];
        for (int i = 0; i < w.length; i++)
            for (int j = 0; j < w.length; j++)
                n[i][j] = w[i][j];
        return n;
    }

	
}
