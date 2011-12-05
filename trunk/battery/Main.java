import java.awt.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;

public class Main {

	public static void main(String[] args) throws Throwable {
		SystemTray tray = SystemTray.getSystemTray();
		System.out.println(tray.getTrayIconSize());
		Image icon = Toolkit.getDefaultToolkit().createImage("icons/battery_charge/battery-good-charging.png");
		TrayIcon trayIcon = new TrayIcon(icon, "battery", null);
		tray.add(trayIcon);
		while (true) {
			System.out.println(
				BatteryStatus.getChargePercentage() + "% " + 
				BatteryStatus.getTimeToLive() + "h");
			Thread.sleep(5000);
		}
	}

}
