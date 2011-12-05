import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Dimension;

public class Main {

	public static void main(String[] args) throws Throwable {
		SystemTray tray = SystemTray.getSystemTray();
		Dimension size = tray.getTrayIconSize();
		TrayIcon trayIcon = new TrayIcon(ImageUtils.textIcon(size, "?"), "battery charge", null);
		tray.add(trayIcon);
		while (true) {
			float charge = BatteryStatus.getChargePercentage();
			System.out.println(
				charge + "% " + 
				BatteryStatus.getTimeToLive() + "h");
			trayIcon.setImage(ImageUtils.textIcon(size, Float.toString(charge)));
			trayIcon.setToolTip(Float.toString(charge) + "%");
			Thread.sleep(5000);
		}
	}

}
