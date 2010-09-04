package net.swined.voyager;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

@SuppressWarnings("serial")
public class Main extends Frame {
	
	private final Image image;
	
	public Main(Image image) {
		this.image = image;
	    setSize(600, 700);
	    addWindowListener(new WindowAdapter(){
	      public void windowClosing(WindowEvent we){
	        System.exit(0);
	      }
	    });
	    setVisible(true);
	}

	public void paint(Graphics g){
	    g.drawImage(image, 0, 25, this);
	}	
	
	public static void main(String[] args) {
		Random random = new Random();
		Point[] points = new Point[100];
		for (int i = 0; i < points.length; i++)
			points[i] = new Point(random.nextInt(600), random.nextInt(600));
		Image image = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().setColor(Color.BLACK);
		int i = -1;
		for (int ix : new Flat(points).getPath()) {
			image.getGraphics().drawArc(points[ix].x - 5, points[ix].y - 5, 10, 10, 0, 360);
//			image.getGraphics().drawString("" + ix, points[ix].x, points[ix].y);
			if (i != -1)
				image.getGraphics().drawLine(
						points[i].x, 
						points[i].y, 
						points[ix].x, 
						points[ix].y);
			i = ix;
		}
		image.flush();
		new Main(image);
	}
	
}
