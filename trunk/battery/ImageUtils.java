import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class ImageUtils {

	private ImageUtils() {
	}

	public static Image textIcon(Dimension dimension, String text) {
		BufferedImage image = new BufferedImage((int)dimension.getWidth(), (int)dimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.drawString(text, 0, 0);
		return image;
	}

}
