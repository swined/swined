package oref;

import java.util.LinkedList;
import java.util.List;

public class Path {

    private final double length;
    private final Point[] path;

    public Path(Point[] path) {
        this.path = path.clone();
        length = updateLength();
    }

    private double updateLength() {
        double r = 0;
        for (int i = 0; i < path.length; i++)
            r += path[i].getDistance(path[(i+1) % path.length]);
        return r;
    }

    public double getLength() {
        return length;
    }

    public Point getPoint(int ix) {
        return path[ix];
    }

    public int getPointCount() {
        return path.length;
    }

    private static boolean inc(int ix[], int m) {
        for (int i = ix.length - 1; i >= 0; i--)
            if (ix[i] + 1 < m) {
                ix[i]++;
                return false;
            } else {
                ix[i] = 0;
            }
        return true;
    }

    public static boolean unequal(int ix[]) {
        for (int i = 0; i < ix.length - 1; i++)
            for (int j = i + 1; j < ix.length; j++)
                if (ix[i] == ix[j])
                    return false;
        return true;
    }

    public Path optimize(int n) {
        int ix[] = new int[n];
        for (int i = 0; i < ix.length; i++)
            ix[i] = 0;
        while (!inc(ix, path.length)) {
            if (!unequal(ix))
                continue;
            int jx[] = new int[n];
            for (int i = 0; i < n; i++)
                jx[i] = 0;
            while (!inc(jx, n)) {
                if (!unequal(jx))
                    continue;
                Point t[] = path.clone();
                for (int i = 0; i < n; i++)
                    t[ix[i]] = path[ix[jx[i]]];
                Path p = new Path(t);
                if (p.getLength() < getLength())
                    return p;
            }
        }
        return null;
    }

    public Path optimize2(int n) {
        for (int i = 0; i < path.length; i++)
            for (int j = 0; j < path.length - n; j++) {
                List<Point> t = new LinkedList();
                for (Point p : path)
                    t.add(p);
                List<Point> tt = new LinkedList();
                for (int k = 0; k < n; k++)
                    tt.add(t.remove(i % t.size()));
                for (Point p : tt)
                    t.add(j, p);
                Point[] ttt = new Point[t.size()];
                for (int ii = 0; ii < t.size(); ii++)
                    ttt[ii] = t.get(ii);
                Path p = new Path(ttt);
                if (p.getLength() < getLength())
                    return p;
            }
        return null;
    }

    public Path deintersect() {
        for (int i = 0; i < path.length - 2; i++)
            for (int j = i + 2; j < path.length; j++);
        return null;
    }

}
