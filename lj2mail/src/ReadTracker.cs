// $Id: ReadTracker.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Collections.Generic;
using System.Data;
using System.Text;
using System.Security.Cryptography;

using Mono.Data.SqliteClient;

    public class ReadTracker {
	protected IDbConnection dbCon;
	protected bool skipUpdates;
	
	public ReadTracker(bool skipUpdates) {
	    this.skipUpdates = skipUpdates;
	    dbCon = (IDbConnection) new SqliteConnection("URI=file:lj2mail.db");
	    dbCon.Open();
	    try {
		runDbCmdNonQuery("CREATE TABLE lj2mail_read(link varchar(64), hash varchar(32));");
	    } catch { }
	}
	
	public void StripRead(RssFeed feed, List<string> links) {
	    links = links ?? new List<string>();
	    List<RssItem> items = feed.Channel.Items;
	    int c = items.Count;
	    foreach (RssItem item in items.FindAll(delegate (RssItem item) {
		return isRead(item);
	    }))
		items.Remove(item);
	    if (c == items.Count) 
		foreach (RssItem item in items.FindAll(delegate (RssItem item) {
		    return ! links.Contains(item.Link);
		})) {
		    MarkRead(item);
		    items.Remove(item);
		}
	}
	
	public void StripReadLinks(List<string> links) {
	    foreach (string link in links.FindAll(delegate (string link) { 
		return isReadLink(link); 
	    }))
		links.Remove(link);
	}	
	
	protected bool isReadLink(string link) {
	    return runDbCmd("SELECT count(*) FROM lj2mail_read WHERE link='" + link + "';") != "0";
	}
	
	protected bool isRead(RssItem item) {
	    if (skipUpdates)
		return isReadLink(item.Link);
	    else 
		return runDbCmd("SELECT count(*) FROM lj2mail_read WHERE link='" + item.Link + "' AND hash='" + hash(item) + "';") != "0";
	}
	
	public void MarkRead(RssItem item) {
	    runDbCmdNonQuery("INSERT INTO lj2mail_read VALUES('" + item.Link + "','" + hash(item) + "');");
	}
	
        protected IDbCommand createDbCmd(string cmdText) {
	    IDbCommand dbCmd = dbCon.CreateCommand();
	    dbCmd.CommandText = cmdText;
	    return dbCmd;
	}
	
	protected void runDbCmdNonQuery(string cmdText) {
	    using (IDbCommand dbCmd = createDbCmd(cmdText)) 
		dbCmd.ExecuteNonQuery();
	}
	
	protected string runDbCmd(string cmdText) {
    	    using (IDbCommand dbCmd = createDbCmd(cmdText))
		using (IDataReader reader = dbCmd.ExecuteReader()) 
		    while(reader.Read()) 
			return reader.GetString(0);
	    return null;
	}
	
	protected string hash(RssItem item) {
	    string str = item.Title + item.Description;
	    byte[] bytes = Encoding.UTF8.GetBytes(str);
	    MD5CryptoServiceProvider provider = new MD5CryptoServiceProvider();
	    byte[] hashBytes = provider.ComputeHash(bytes);
	    string hash = "";
	    foreach (byte c in hashBytes) 
		hash += c.ToString("x2");
	    return hash;
	}
    }
