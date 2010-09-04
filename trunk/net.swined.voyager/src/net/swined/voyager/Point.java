package net.swined.voyager;

public class Point {

	public final int x;
	public final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int distance(Point p) {
		return (int) Math.sqrt((p.x - x) * (p.x - x) + (p.y - y) * (p.y - y));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
}
