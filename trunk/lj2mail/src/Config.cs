// $Id: Config.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.IO;
using System.Reflection;
using System.Collections.Generic;

public class Config {
	protected Dictionary<string, string> raw = new Dictionary<string, string>();
	public string this[string name] {
		get {
			return raw[name];
		}
		set {
			raw[name] = value;
		}
	}
	
	// general settings
	public readonly string LjUser = null;
	public readonly string LjPass = null;
	public readonly string Site = "livejournal.com";
	public readonly int Timeout = 5;
	public readonly bool SkipUpdates = true;
	public readonly int EntryWidth = 600;
	public readonly string MailFrom = "LJ Notifier <" + Environment.UserName + "@" + Environment.MachineName + ">";
	public readonly string MailTo = null;
	public readonly bool FastCheck = true;
	public readonly bool Loop = false;
	public readonly bool UseBase64 = true;	
	public readonly int Wait = 0;
	public readonly int SleepOnFail = 0;
	public readonly bool Quiet = true;
	public readonly bool Debug = false;
	
	// formatting settings
	public readonly string SubjectFormat = "{$this.Journal}: {$entry.Title ?? untitled} {$entry.TagsText}";
	public readonly string BtnTrackImgUrl = "http://stat.livejournal.com/img/btn_track.gif";
	public readonly string BtnAddImgUrl = "http://stat.livejournal.com/img/btn_addfriend.gif";
	public readonly string BtnMemImgUrl = "http://stat.livejournal.com/img/btn_memories.gif";
	public readonly string UserInfoImgUrl = "http://stat.livejournal.com/img/userinfo.gif";
	public readonly string AddLink = "http://{$conf.Site}/friends/add.bml?user={$this.Journal}";
	public readonly string MemLink = "http://{$conf.Site}/tools/memadd.bml?journal={$this.Journal}&itemid={$entry.Id}";
	public readonly string TrackLink = "http://{$conf.Site}/manage/subscriptions/entry.bml?journal={$this.Journal}&itemid={$entry.Id}";
	public readonly string BtnAddFormat = "<a href=\"{#$conf.AddLink}\"><img src=\"{$conf.BtnAddImgUrl}\"></a>";
	public readonly string BtnMemFormat = "<a href=\"{#$conf.MemLink}\"><img src=\"{$conf.BtnMemImgUrl}\"></a>";
	public readonly string BtnTrackFormat = "<a href=\"{#$conf.TrackLink}\"><img src=\"{$conf.BtnTrackImgUrl}\"></a>";
	public readonly string ToolbarFormat = "<div style=\"float: left; margin-right: 10px;\">{#$conf.BtnAddFormat}&nbsp;{#$conf.BtnMemFormat}&nbsp;{#$conf.BtnTrackFormat}</div>";
	public readonly string HeadFormat = "<a href=\"{$channel.ProfileLink}\">{$channel.Title}</a><br>" + 
		"{$entry.PubDateLocal}<br>{$entry.LjSecurityHtml}<b><a href=\"{$entry.Link}\">" +
		"{$entry.Title ?? untitled}</a></b><br>{$entry.LjMusicHtml}{$entry.LjMoodHtml}{$entry.TagsHtml}";
	public readonly string UpicFormat = "<div style=\"float: right; margin-left: 10px; margin-right: 10px;\">{$channel.ImageHtml}" +
		"<br><a href=\"{$channel.Link}\"><img src=\"{$conf.UserInfoImgUrl}\" align=\"absmiddle\"><b>{$this.Journal}</b></a></div>";
	public readonly string MessageFormat = "<div style=\"width: {$conf.EntryWidth}px;" + 
		"\">{#$conf.ToolbarFormat}{#$conf.UpicFormat}{#$conf.HeadFormat}<hr>{$entry.Description}</div>";
	
	// code
	public Config(string fileName) {
		using (TextReader sr = new StreamReader(fileName))
			foreach (string c in sr.ReadToEnd().Split('\n')) {
				if (string.IsNullOrEmpty(c))
					continue;
				int p = c.IndexOf('=');
				if (p == -1) 
					throw new Exception("Invalid config file syntax: " + c);
				setProp(c.Substring(0, p), c.Substring(p + 1));
			}
		checkAllStrPropsSet();
	}

	protected void setProp(string name, string val) {
		FieldInfo f = this.GetType().GetField(name);
		if (f == null) {
			if (name[0] != '+')
				throw new Exception(name + " is not a valid configuration parameter");
			raw[name.Remove(0, 1)] = val;
			return;
		}
		try {
			switch (f.FieldType.ToString()) {
				case "System.String":
					f.SetValue(this, val);
					break;
				case "System.Int32":
					f.SetValue(this, int.Parse(val));
					break;
				case "System.Boolean":
					f.SetValue(this, bool.Parse(val));
					break;
				default:
					throw new Exception("Unsupported field type: " + f.FieldType.ToString());
			}
		} catch (Exception e) {
			throw new Exception("Failed to set " + name + " (" + f.FieldType.ToString() + "): " + e.Message);
		}
	}

	protected void checkAllStrPropsSet() {
		foreach (FieldInfo field in this.GetType().GetFields())
			if ((field.FieldType == typeof(string)) && string.IsNullOrEmpty(field.GetValue(this) as string))
				throw new Exception(field.Name + " is not set");
	}
}
