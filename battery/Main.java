import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Dimension;

public class Main {

	public static void main(String[] args) throws Throwable {
		SystemTray tray = SystemTray.getSystemTray();
		Dimension size = tray.getTrayIconSize();
		TrayIcon timeIcon = new TrayIcon(ImageUtils.textIcon(size, "?"), "battery time", null);
		BatteryChargeIcon batteryChargeIcon = new BatteryChargeIcon(tray);
		tray.add(timeIcon);
		System.out.println(Utils.getCanvas(timeIcon));
		while (true) {
			float time = BatteryStatus.getTimeToLive();
			batteryChargeIcon.update();
			timeIcon.setImage(ImageUtils.textIcon(size, Float.toString(time)));
			timeIcon.setToolTip(Float.toString(time) + "h");
			Thread.sleep(10000);
		}
	}

}
