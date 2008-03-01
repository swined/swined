import java.lang.Exception;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Formatter {
	protected class ObjectMap extends HashMap<String, IProperties> { 
		static final long serialVersionUID = 0;
	}
	protected static Pattern pTriOp = Pattern.compile("^(.+) \\? (.+) : (.+?)$");
	protected static Pattern pDefVal = Pattern.compile("^(.+) \\?\\? (.+?)$");
	protected static Pattern pEval = Pattern.compile("^ *#(.+) *$");
	protected static Pattern pVar = Pattern.compile("^ *\\$([^ ]+) *$");
	protected static Pattern pEsc = Pattern.compile("(.)");
	protected static Pattern pUnesc = Pattern.compile("\\\\(.)");
	protected static Pattern pFormat = Pattern.compile(
		"^((?:|.*?[^\\\\])(?:\\\\\\\\)*?)\\{(.*?[^\\\\](?:|\\\\\\\\)*)\\}(.*)$",
		Pattern.DOTALL
	);
	protected ObjectMap objects = new ObjectMap();

	protected static String var(ObjectMap objects, String name) throws Exception {
		if (name == null) 
			return "[null var name]";
		String[] path = name.split("\\.", 2);
		if (path.length != 2)
			return "[bad var name: " + name + "]";
		IProperties object = objects.get(path[0]);
		return object == null ? 
			"[unknown object: " + path[0] + "]" : 
			object.get(path[1]);
	}

	protected static String eval(ObjectMap obj, String text) throws Exception {
		if (text == null)
			return null;
		Matcher m = pTriOp.matcher(text);
		if (m.matches()) 
			return Util.strNullOrEmpty(eval(obj, m.group(1))) ? 
				eval(obj, m.group(3)) : 
				eval(obj, m.group(2));
		m = pDefVal.matcher(text);
		if (m.matches()) {
			String v1 = eval(obj, m.group(1));
			return Util.strNullOrEmpty(v1) ? eval(obj, m.group(2)) : v1;
		}
		m = pEval.matcher(text);
		if (m.matches())
		    return format(eval(obj, m.group(1)), obj);
		m = pVar.matcher(text);
		if (m.matches()) 
			return var(obj, m.group(1));
		return text;
	}

	protected static String escape(String t) {
		return pEsc.matcher(t).replaceAll("\\\\$1");
	}

	protected static String unescape(String t) {
		return pUnesc.matcher(t).replaceAll("$1");
	}

	protected static String format(String format, ObjectMap obj) throws Exception {
		if (format == null)
			return "[null]";
		String r = "";
		while (true) {
			Matcher m = pFormat.matcher(format);
			if (!m.matches()) {
				r += format;
				break;
			}
			r += m.group(1) + escape(eval(obj, unescape(m.group(2))));
			format = m.group(3);
		}
		return unescape(r);
	}

	public void addObject(String name, IProperties object) {
		objects.put(name, object);
	}

	public String format(String format) throws Exception {
		return format(format, objects);
	}
}
