// $Id: UpdateTracker.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Collections.Generic;
using System.Data;

using Mono.Data.SqliteClient;

    public class UpdateTracker {
	protected LjClient ljc;
	protected Config conf;
	protected IDbConnection dbcon;
	
	public UpdateTracker(Config conf, LjClient ljClient) {
	    ljc = ljClient;
	    this.conf = conf;
	    dbcon = (IDbConnection) new SqliteConnection("URI=file:lj2mail.db");
	    dbcon.Open();
	    try {
		runDbCmdNonQuery("CREATE TABLE lj2mail_lastupdate(journal varchar(32), lastupdate varchar(64), newupdate varchar(64));");
	    } catch { }
	}
	
	public List<string> GetUpdated() {
	    return GetUpdated(ljc.GetFriends());
	}
	
	public List<string> GetUpdated(List<string> friends) {
		List<string> r = new List<string>();
		foreach (string j in friends) {
			try {
				if (!conf.Quiet)
					Console.WriteLine("Checking " + j);
				DateTime lu = ljc.GetLastUpdate(j);
				DateTime su;
				if (!DateTime.TryParse(getLastUpdate(j), out su)) {
					su = default(DateTime);
					if (!conf.Quiet)
						Console.WriteLine("Failed to parse " + j + "'s update timestamp. Assuming default. ");
				}
				if (su < lu) {
					runDbCmdNonQuery("UPDATE lj2mail_lastupdate SET newupdate='" + lu.ToString() + "' WHERE journal='" + j + "';");
					r.Add(j);
				}
			} catch (Exception e) {
				if (!conf.Quiet)
					Console.WriteLine("Failed to check " + j + ": " + e.Message);
			}
		}
		return r;
	}
	
	protected string getLastUpdate(string journal) {
	    string r = runDbCmd("SELECT lastupdate FROM lj2mail_lastupdate WHERE journal='" + journal + "';");
	    if (r != null) return r;
	    runDbCmdNonQuery("INSERT INTO lj2mail_lastupdate VALUES('" + journal + "','" + default(DateTime).ToString() + "','" + default(DateTime).ToString() + "')");
	    return default(DateTime).ToString();
	}
	
	public void CommitUpdate(string journal) {
	    runDbCmdNonQuery("UPDATE lj2mail_lastupdate SET lastupdate=newupdate WHERE journal='" + journal + "';");
	}
	
        protected IDbCommand createDbCmd(string cmdText) {
	    IDbCommand dbcmd = dbcon.CreateCommand();
	    dbcmd.CommandText = cmdText;
	    return dbcmd;
	}
	
	protected void runDbCmdNonQuery(string cmdText) {
	    using (IDbCommand dbcmd = createDbCmd(cmdText)) 
		dbcmd.ExecuteNonQuery();
	}
	
	protected string runDbCmd(string cmdText) {
    	    using (IDbCommand dbcmd = createDbCmd(cmdText))
		using (IDataReader reader = dbcmd.ExecuteReader()) 
		    while(reader.Read()) 
			return reader.GetString(0);
	    return null;
	}
    }
