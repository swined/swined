// $Id: Formatter.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections.Generic;
using System.Reflection;

public class Formatter {
	public Dictionary<string, object> Objects = new Dictionary<string, object>();
	
	protected PropertyInfo getProp(object obj, string name) {
		PropertyInfo prop = obj.GetType().GetProperty(name);
		if ((prop == null) || (prop.GetIndexParameters().Length != 0))
			return null;
		return prop;
	}
	
	protected string var(string name) {
		string[] n = name.Split('.');
		if (n.Length != 2) return "";
		if (!Objects.ContainsKey(n[0])) return "";
		object obj = Objects[n[0]];
		if (obj == null) return "";
		FieldInfo field = obj.GetType().GetField(n[1]);
		if (field == null) {
			PropertyInfo prop = getProp(obj, n[1]);
			if (prop == null) return "";
			return (prop.GetValue(obj, null) ?? "").ToString();
		}
		return (field.GetValue(obj) ?? "").ToString();
	}
	
	protected string eval(string prog) {
		Match match;
		match = Regex.Match(prog, @"^(.*) \? (.*) : (.*?)$");
		if (match.Groups.Count > 1) {
			string v1 = eval(match.Groups[1].Value);
			return string.IsNullOrEmpty(v1) ? eval(match.Groups[3].Value) : eval(match.Groups[2].Value);
		}
		match = Regex.Match(prog, @"^(.*) \?\? (.*?)$");
		if (match.Groups.Count > 1) {
			string v1 = eval(match.Groups[1].Value);
			return string.IsNullOrEmpty(v1) ? eval(match.Groups[2].Value) : v1;
		}
		match = Regex.Match(prog, @"^ *#(.*)$");		
		if (match.Groups.Count > 1) 
			return Format(eval(match.Groups[1].Value));
		match = Regex.Match(prog, @"^ *\$([a-zA-Z0-9.]+) *$");
		if (match.Groups.Count > 1) 
			return var(match.Groups[1].Value);
		return prog;
	}
	
	protected string unescape(string str) {
		return Regex.Replace(str, @"\\(.)", "$1");
	}
        
	protected string escape(string str) {
            return Regex.Replace(str, "(.)", @"\\$1");
        }
        
	public string Format(string format) {
            return unescape(Regex.Replace(format, @"((?:|[^\\](?:\\\\)*)){(.*?[^\\](?:|\\\\)*)}",
                delegate (Match match) { return match.Groups[1].Value + escape(eval(unescape(match.Groups[2].Value)) ?? ""); } ));
	}
}
