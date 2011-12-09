import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.awt.Canvas;

public class Utils {

	private Utils() {}

	public static Canvas getCanvas(TrayIcon icon) throws Throwable {
		Object peer = getPeer(icon);
		return (Canvas)getField(peer, "canvas");
	}

	@SuppressWarnings("rawtypes")
	public static Object getPeer(TrayIcon icon) throws Throwable {
		Object awtAutoShutdown = Class.forName("sun.awt.AWTAutoShutdown").getMethod("getInstance").invoke(null);
		Map peerMap = (Map)getField(awtAutoShutdown, "peerMap");
		return peerMap.get(icon);
	}

	public static Object getField(Object target, String name) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
		Field field = target.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return field.get(target);
	}

}
