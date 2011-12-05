import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BatteryStatus {

	private static final File BASE = new File("/sys/class/power_supply/battery");

	private static int getInt(String file) throws IOException {
		FileInputStream in = new FileInputStream(new File(BASE, file));
		try {
			int r = 0;
			while (true) {
				int c = in.read();
				if (c < 0)
					break;
				c -= 48;
				if (c >= 0 && c <= 9)
					r = r * 10 + c; 
			}
			return r;
		} finally {
			in.close();
		}
	}

	public static float getChargeFull() throws IOException {
		return getInt("charge_full");
	}

	public static float getChargeFullDesign() throws IOException {
		return getInt("charge_full_design");
	}

	public static float getChargeNow() throws IOException {
		return getInt("charge_now");
	}

	public static float getCurrentNow() throws IOException {
		return getInt("current_now");
	}

	public static float getChargePercentage() throws IOException {
		return 100 * getChargeNow() / getChargeFullDesign();
	}

	public static float getTimeToLive() throws IOException {
		return getChargeNow() / getCurrentNow();
	}

}
