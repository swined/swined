package net.swined.voyager;

public class Point {

	public final double x;
	public final double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distance(Point p) {
		return Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
