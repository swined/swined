package net.swined.voyager;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Random random = new Random();
		Point[] points = new Point[20];
		for (int i = 0; i < points.length; i++)
			points[i] = new Point(random.nextDouble(), random.nextDouble());
		for (int ix : new Flat(points).getPath())
			System.out.println(points[ix]);
	}
	
}
