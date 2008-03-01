import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.Properties;
import java.lang.reflect.Field;

public class Config implements IProperties {
	public final String LjUser = null;
	public final String LjPass = null;
	public final String Site = "www.livejournal.com";
	public final Boolean Quiet = false;
	public final Boolean Debug = true;

	protected final static long serialVersionUID = 0;
	protected final Properties properties = new Properties();
	protected final String fileName;

	protected void setField(Field field) throws EBadSyntax {
		String val = get(field.getName());
		if (val == null)
			return;
		try {
			field.setAccessible(true);
			field.set(this, field.getType().getConstructor(String.class).newInstance(val));
			field.setAccessible(false);
		} catch (Exception e) {
			throw new EBadSyntax("failed to set Config." + field.getName() + ": " + val + " [" + fileName + "]");
		}
	}

	protected Config(File file) throws 
	EUndefinedProperty,
	EBadSyntax,
	IOException,
	IllegalAccessException {
		fileName = file.getName(); 
		properties.load(new FileInputStream(file));
		Field[] fields = this.getClass().getFields();
		for (int i = 0; i < fields.length; i++) { 
			setField(fields[i]);
			if (fields[i].get(this) == null)
				throw new EUndefinedProperty(fields[i].getName() + " config property is not set [" + fileName + "]");
		}
	}
	
	public String get(String name) {
		return properties.getProperty(name);
	}

	public static Vector<Config> load(String path) throws 
	EUndefinedProperty,
	EBadSyntax,
	IOException,
	IllegalAccessException {
		String[] files = (new File(path)).list();
		Vector<Config> result = new Vector<Config>();
		for (int i = 0; i < files.length; i++)
			if (files[i].endsWith(".conf")) 
				result.add(new Config(new File(path, files[i])));
		return result;
	}
}
