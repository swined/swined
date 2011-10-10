package org.swined.pdf2img;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFRenderer;

public class Main {

	private static PDFFile loadPdf(String path) throws IOException {
		File file = new File(path);
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		return new PDFFile(buf);
	}
	
	private static void render(String in, String prefix, float zoom) throws IOException {
		PDFFile pdf = loadPdf(in);
		int np = pdf.getNumPages();
		int nl = Integer.toString(np).length();
		for (int i = 1; i <= np; i++) {
			System.out.println("Extracting page " + i + " of " + np);
			PDFPage page = pdf.getPage(i);
			RenderedImage img = render(page, zoom);
			File yourImageFile = new File(prefix + pad(i, nl) + ".png");
			ImageIO.write(img, "png", yourImageFile);
		}
		System.out.println("Done");
	}

	private static String pad(int n, int l) {
		String s = Integer.toString(n);
		while (s.length() < l)
			s = "0" + s;
		return s;
	}

	private static RenderedImage render(PDFPage page, float zoom) {
		int width = (int)(page.getWidth() * zoom);
		int height = (int)(page.getHeight() * zoom);
		Rectangle bounds = new Rectangle(0, 0, width, height);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getGraphics().setColor(Color.WHITE);
		image.getGraphics().fillRect(0, 0, width, height);
		PDFRenderer renderer = new PDFRenderer(page, image.createGraphics(), bounds, null, null);
		renderer.run();
		renderer.cleanup();
		return image;
	}

	public static void main(final String[] args) throws NumberFormatException, IOException {
		render(args[0], "page_", Float.parseFloat(args[1]));
	}
}