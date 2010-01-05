package oref;

public class Path {

    private final Point[] path;

    public Path(Point[] path) {
        this.path = path.clone();
    }

    public Point[] getPath() {
        return path.clone();
    }

    public double getLength() {
        double r = 0;
        for (int i = 0; i < path.length; i++)
            r += path[i].getDistance(path[(i+1) % path.length]);
        return r;
    }

    public Path optimize() {
        double l = getLength();
        for (int i = 0; i < path.length - 1; i++)
            for (int j = i + 1; j < path.length; j++) {
                Point[] t = path.clone();
                t[i] = path[j];
                t[j] = path[i];
                Path p = new Path(t);
                if (p.getLength() < l)
                    return p;
            }
        return null;
    }

}
