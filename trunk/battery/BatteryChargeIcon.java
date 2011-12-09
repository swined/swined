import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Dimension;
import java.awt.AWTException;
import java.awt.Color;

public class BatteryChargeIcon {

	private final Image batteryMissing = getImage("battery-missing.png"); 
	private final Image[] images = {
		getImage("battery-caution.png"),
		getImage("battery-low.png"),
		getImage("battery-040.png"),
		getImage("battery-060.png"),
		getImage("battery-080.png"),
		getImage("battery-100.png"),

	};
	private final TrayIcon icon;

	public BatteryChargeIcon(SystemTray tray) throws Throwable { //AWTException {
		this.icon = new TrayIcon(batteryMissing);
		tray.add(icon);
		Utils.getCanvas(icon).setBackground(new Color(100, 100, 100, 0));
		icon.setImageAutoSize(true);
	}

	public void update() throws IOException {
		float charge = BatteryStatus.getChargePercentage();
		int image = Math.round(charge * (images.length - 1) / 100);
		icon.setImage(images[image]);
		icon.setToolTip(String.format("%f%%", charge, image));
	}

	private static Image getImage(String name) {
		return Toolkit.getDefaultToolkit().createImage(Main.class.getResource("icons/" + name));
	}
	
}
