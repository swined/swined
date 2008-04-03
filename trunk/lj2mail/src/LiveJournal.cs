// $Id: LiveJournal.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Net;
using System.Text;
using System.Text.RegularExpressions;

    public class LjClient
    {
	protected Config conf;
	protected string ljServer;

	public LjClient(Config conf) {
		this.conf = conf;
		ljServer = "http://" + conf.Site + "/interface/flat";
	}
	
        public NameValueCollection Request(string mode, NameValueCollection values)
        {
            WebClient wc = new WebClient();
            if (values == null)
                values = new NameValueCollection();
            values["user"] = conf.LjUser;
            values["auth_method"] = "clear";
            values["password"] = conf.LjPass;
            values["mode"] = mode;
            values["ver"] = "1";
            string s = Encoding.UTF8.GetString(wc.UploadValues(ljServer, values));
            string[] v = s.Split('\n');
            string n = "";
            NameValueCollection r = new NameValueCollection();
            for (int i = 0; i < v.Length; i++)
                if (i % 2 == 0) n = v[i];
                else r[n] = v[i];
            if (r["success"] != "OK")
                throw new Exception(r["errmsg"]);
            return r;
        }
	
	public DateTime GetLastUpdate(string journal) {
	    WebClient wc = new WebClient();
	    string url = "http://" + conf.Site + "/misc/weblogs-change.bml?user=" + journal;
	    if (!conf.Quiet)
	    	if (conf.Debug)
			Console.WriteLine("GetLastUpdate(" + url + ")");
	    wc.DownloadString(url);
	    try {
		return DateTime.Parse(wc.ResponseHeaders["Last-Modified"]);
	    } catch {
		return default(DateTime);
	    }
	}
	
	public bool IsModified(string journal, DateTime timestamp) {
	    return timestamp < GetLastUpdate(journal);
	}
	
        public List<string> GetFriends()
        {
            List<string> r = new List<string>();
            NameValueCollection ljr = Request("getfriends", null);
            int c = int.Parse(ljr["friend_count"]);
            for (int i = 1; i <= c; i++)
                if (ljr["friend_" + i + "_status"] != "deleted")
                    r.Add(ljr["friend_" + i + "_user"]);
            return r;
        }

	protected string getRealFeedUrl(string journal) {
		HttpWebRequest rq = WebRequest.Create("http://" + conf.Site + "/~" + journal + "/rss") as HttpWebRequest;
		rq.Timeout = conf.Timeout * 1000;		
		HttpWebResponse rs = rq.GetResponse() as HttpWebResponse;
		string r = rs.ResponseUri.ToString();
		return r;
	}

	public RssFeed GetRss(string journal) {
		string url = getRealFeedUrl(journal);
		bool isInt = Regex.IsMatch(url, conf.Site);
		if (isInt)
			url = url + "?auth=digest";
		HttpWebRequest rq = WebRequest.Create(url) as HttpWebRequest;
		rq.Timeout = conf.Timeout * 1000;	
		if (isInt)
			rq.Credentials = new NetworkCredential(conf.LjUser, conf.LjPass);
		HttpWebResponse rs = rq.GetResponse() as HttpWebResponse;
		return RssFeed.Deserialize(rs.GetResponseStream());
	}
	
	public List<string> GetLastUpdatedEntries() {
	    byte[] data = Encoding.UTF8.GetBytes("user=" + conf.LjUser + "&password=" + conf.LjPass);
	    HttpWebRequest rq = WebRequest.Create("http://" + conf.Site + "/mobile/login.bml") as HttpWebRequest;
	    rq.AllowAutoRedirect = false;
	    rq.Method = "POST";
	    rq.ContentType = "application/x-www-form-urlencoded";
	    rq.ContentLength = data.Length;
	    rq.GetRequestStream().Write(data, 0, data.Length);
	    HttpWebResponse rs = rq.GetResponse() as HttpWebResponse;
	    string cookies = "";
	    bool seenSess = false;
	    foreach (string header in rs.Headers.GetValues("Set-Cookie")) {
		string cookie = header.Split(';')[0] + "; ";
		cookies += cookie;
		if (cookie.Split('=')[0] == "ljsession")
		    seenSess = true;
	    }
	    if (!seenSess)
		throw new Exception("Not logged in");
	    WebClient wc = new WebClient();
	    wc.Headers["Cookie"] = cookies;
	    string s = wc.DownloadString("http://" + conf.Site + "/mobile/friends.bml");
	    List<string> ls = new List<string>();
	    foreach (Match match in Regex.Matches(s,": <a href='(http://.*?)\\?format=light'>")) {
		string v = match.Groups[1].Value;
		ls.Add(v);
	    }
	    return ls;
	}
	
	public List<string> GetLastUpdatedFriends() {
	    return GetUsersFromUrls(GetLastUpdatedEntries());
	}
	
	public List<string> GetUsersFromUrls(List<string> urls) {
	    NameValueCollection r = new NameValueCollection();
	    foreach (string s in urls) 
		r[GetUserFromUrl(conf, s)] = "*";
	    List<string> ls = new List<string>();
    	    foreach (string s in r.Keys) 
		ls.Add(s);
	    return ls;
	}
	
	public static string GetUserFromUrl(Config conf, string url) {
	    if (Regex.IsMatch(url,"http://(.*?)\\." + conf.Site + "/[0-9]*\\.html")) 
		return Regex.Match(url,"http://(.*?)\\." + conf.Site + "/[0-9]*\\.html").Groups[1].Value;
	    if (Regex.IsMatch(url,"http://(users|community|syndicated)\\." + conf.Site + "/(.*?)/[0-9]*\\.html")) 
		return Regex.Match(url,"http://(users|community|syndicated)\\." + conf.Site + "/(.*?)/[0-9]*\\.html").Groups[2].Value;
	    return null;
	}
    }
