import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Dimension;

public class Main {

	public static void main(String[] args) throws Throwable {
		SystemTray tray = SystemTray.getSystemTray();
		Dimension size = tray.getTrayIconSize();
		TrayIcon chargeIcon = new TrayIcon(ImageUtils.textIcon(size, "?"), "battery charge", null);
		TrayIcon timeIcon = new TrayIcon(ImageUtils.textIcon(size, "?"), "battery time", null);
		tray.add(chargeIcon);
		tray.add(timeIcon);
		while (true) {
			float charge = BatteryStatus.getChargePercentage();
			float time = BatteryStatus.getTimeToLive();
			chargeIcon.setImage(ImageUtils.textIcon(size, Float.toString(charge)));
			chargeIcon.setToolTip(Float.toString(charge) + "%");
			timeIcon.setImage(ImageUtils.textIcon(size, Float.toString(time)));
			timeIcon.setToolTip(Float.toString(time) + "h");
			Thread.sleep(5000);
		}
	}

}
