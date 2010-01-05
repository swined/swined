package oref;

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

    private boolean worthSwap(int i, int j) {
        Path p = swap(i, j);
        return p.getLength() < getLength();
        /*
        final int im = (i - 1 + path.length) % path.length;
        final int ip = (i + 1 + path.length) % path.length;
        final int jm = (j - 1 + path.length) % path.length;
        final int jp = (j + 1 + path.length) % path.length;
        final double r =
            10 +
            path[im].getDistance(path[i]) +
            path[ip].getDistance(path[i]) +
            path[jm].getDistance(path[j]) +
            path[jp].getDistance(path[j]) -
            path[im].getDistance(path[j]) -
            path[ip].getDistance(path[j]) -
            path[jm].getDistance(path[i]) -
            path[jp].getDistance(path[i]);
        return r > 10;
         */
    }

    private Path swap(int i, int j) {
        Point[] t = path.clone();
        t[i] = path[j];
        t[j] = path[i];
        return new Path(t);
    }

    public Path optimize() {
        for (int i = 0; i < path.length - 1; i++)
            for (int j = i + 1; j < path.length; j++)
                if (worthSwap(i, j))
                    return swap(i, j);
        return null;
    }

}
