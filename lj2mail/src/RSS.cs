// $Id: RSS.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.IO;
using System.Collections.Generic;
using System.Xml.Serialization;
using System.Xml;
using System.Text.RegularExpressions;

    [XmlRoot(ElementName = "rss")]
    public class RssFeed {
	[XmlElement(ElementName = "channel")]	    
	public RssChannel Channel = null;
	protected static XmlSerializer serializer = null;
	
	protected static void InitSerializer() {
	    serializer = serializer ?? new XmlSerializer(typeof(RssFeed));
	}
		
	public static RssFeed Deserialize(Stream stream) {
	    InitSerializer();
	    return (RssFeed)(serializer.Deserialize(stream));
	}
	
	public static RssFeed Deserialize(byte[] feedBytes) {
	    return Deserialize(new MemoryStream(feedBytes));
	}
	
	public void Serialize(Stream stream) {
	    InitSerializer();
	    serializer.Serialize(stream,this);
	}
	
	public void Serialize(string filename) {
	    using (StreamWriter sw = new StreamWriter(filename)) 
		Serialize(sw.BaseStream);
	}
    }

    public class RssChannel {
	[XmlElement(ElementName = "title")]	
	public string Title = null;
	[XmlElement(ElementName = "link")]	
	public string Link = null;
	[XmlElement(ElementName = "description")]	
	public string Description = null;
	[XmlElement(ElementName = "managingEditor")]	
	public string ManagingEditor = null;
	[XmlElement(ElementName = "lastBuildDate")]	
	public string LastBuildDate = null;
	[XmlElement(ElementName = "generator")]	
	public string Generator = null;
	[XmlElement(ElementName = "image")]	
	public RssImage Image = null;
	[XmlElement(ElementName = "item")]	    
	public List<RssItem> Items = null;
	public string ImageHtml {
	    get {
		return Image == null ? "" : Image.Html;
	    }
	}
	public string ProfileLink {
	    get {
		return Link + (Link[Link.Length-1] == '/' ? "" : "/") + "profile";
	    }
	}
    }

    public class RssItem {
	[XmlElement(ElementName = "guid")]	
	public string Guid = null;
	[XmlElement(ElementName = "pubDate")]
	public string PubDate = null;
	[XmlElement(ElementName = "title")]
	public string Title = null;
	[XmlElement(ElementName = "author")]
	public string Author = null;
	[XmlElement(ElementName = "link")]
	public string Link = null;
	[XmlElement(ElementName = "description")]
	public string Description = null;
	[XmlElement(ElementName = "comments")]
	public string Comments = null;
	[XmlElement(ElementName = "category")]	    
	public List<string> Categories = null;
	[XmlAnyElement]
	public XmlElement[] Unknown = null;
	public string this[string name] {
	    get {
		foreach (XmlElement element in Unknown ?? new XmlElement[0])
		    if (element.Name.ToLower() == name.ToLower())
			return element.InnerText;
		return null;
	    }
	}
	public string Tags {
	    get {
		if (Categories == null || Categories.Count == 0)
		    return "";
		string r = Categories[0];
		bool f = true;
		foreach (string cat in Categories) {
		    if (f) {
			f = false;
			continue;
		    }
		    r += ", " + cat;
		}
		return r;
	    }
	}
	public string TagsHtml {
	    get {
		string tags = Tags;
		if (string.IsNullOrEmpty(Tags))
		    return "";
		return "Tags: " + tags + "<br>";
	    }
	}
	public string TagsText {
	    get {
		string tags = Tags;
		if (string.IsNullOrEmpty(Tags))
		    return "";
		return "[" + tags + "]";
	    }
	}
	public string LjSecurityHtml {
	    get {
		string s = this["lj:security"];
		if (string.IsNullOrEmpty(s))
		    return "";
		switch (s) {
		    case "public": return "";
		    case "usemask": return "<img src=\"http://stat.livejournal.com/img/icon_protected.gif\" align=\"absmiddle\">&nbsp;";
		    default: return "[" + s + "]&nbsp;";
		}
	    }
	}
	public string LjSecurity {
	    get {
		string s = this["lj:security"];
		if (string.IsNullOrEmpty(s))
		    return "";
		switch (s) {
		    case "public": return "";
		    case "usemask": return "friends";
		    default: return s;
		}
	    }
	}
	public string PubDateLocal {
	    get {
		try {
		    return DateTime.Parse(PubDate).ToString();
		} catch {
		    return PubDate;
		}
	    }
	}
	public string LjMoodHtml {
	    get {
		return string.IsNullOrEmpty(this["lj:mood"]) ? "" : "Mood: " + this["lj:mood"] + "<br>";
	    }
	}
	public string LjMusicHtml {
	    get {
		return string.IsNullOrEmpty(this["lj:music"]) ? "" : "Music: " + this["lj:music"] + "<br>";
	    }
	}
	public string Id {
	    get {
		return Regex.Match(Link, "/([0-9]*)\\.html").Groups[1].Value;
	    }
	}
    }
    
    public class RssImage {
	[XmlElement(ElementName = "url")]	
	public string Url = null;
	[XmlElement(ElementName = "title")]	
	public string Title = null;
	[XmlElement(ElementName = "link")]	
	public string Link = null;
	[XmlElement(ElementName = "width")]	
	public string Width = null;
	[XmlElement(ElementName = "height")]	
	public string Height = null;
	public string Html {
	    get {
		if (Url == null)
		    return null;
		string r = "<img src=\"" + Url + "\"";
		if (Title != null) r += " alt=\"" + Title + "\"";
		if (Width != null) r += " width=\"" + Width + "\"";	    
		if (Height != null) r += " height=\"" + Height + "\"";
		r += ">";
	        if (Url != null)
		    r = "<a href=\"" + Url + "\">" + r + "</a>";
		return r;
	    }
	}
    }
