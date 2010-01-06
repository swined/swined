package oref;

public class Point {

    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistance(Point p) {
        return Math.sqrt((p.x - x)*(p.x - x)+(p.y - y)*(p.y - y));
    }

}
