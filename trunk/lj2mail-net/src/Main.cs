// $Id: Main.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Collections.Generic;
using System.Threading;

namespace @namespace {
    public class @class {
	public static void Main() {
	    try {
		Config conf = new Config("lj2mail.conf");
		if (!conf.Quiet)
			Console.WriteLine("Initializing");
		LjClient ljc = new LjClient(conf);
		UpdateTracker ut = new UpdateTracker(conf, ljc);
		ReadTracker rt = new ReadTracker(conf.SkipUpdates);
		Mailer ml = new Mailer(conf);
		do {
		    if (conf.Wait != 0) {
		    	if (!conf.Quiet)
			Console.WriteLine("Sleeping " + conf.Wait + "sec");
			Thread.Sleep(conf.Wait * 1000);
		    }
		    List<string> upd, updLinks;
		    if (!conf.FastCheck) {
		    	if (!conf.Quiet)
				Console.WriteLine("Retrieving friends list");
			upd = ljc.GetFriends();
			updLinks = new List<string>();
		    } else {
			try {
				if (!conf.Quiet)
			    		Console.WriteLine("Retrieving last updated friends");
			    updLinks = ljc.GetLastUpdatedEntries();
			    if (conf.SkipUpdates) 
				rt.StripReadLinks(updLinks);
				if (!conf.Quiet)
					Console.WriteLine(updLinks.Count + " new entries found");
				if (!conf.Quiet)
				if (conf.Debug) 
					foreach (string ent in updLinks)
						Console.WriteLine(ent);
			    upd = ljc.GetUsersFromUrls(updLinks);
			    if (upd.Count == 0) 
				continue;
			} catch(Exception e) {
				if (!conf.Quiet) 
					Console.WriteLine("Failed to fetch update list: " + e.Message);
			    continue;
			}
		    }
		    if (!conf.Quiet)
		    Console.WriteLine("Checking for updates");
		    foreach (string f in ut.GetUpdated(upd)) 
			try {
				if (!conf.Quiet)
			    	Console.WriteLine("Loading "+f+"'s rss");
			    RssFeed feed = ljc.GetRss(f);
			    rt.StripRead(feed, updLinks);
			    if (feed.Channel.Items.Count > 0)
			    	if (!conf.Quiet)
					Console.WriteLine("Sending "+feed.Channel.Items.Count.ToString()+" items");
			    ml.SendMail(f,feed);
			    foreach (RssItem item in feed.Channel.Items)
				rt.MarkRead(item);
			    ut.CommitUpdate(f);
			} catch (Exception e) {
				if (!conf.Quiet)
					Console.WriteLine("Error loading " + f + "'s rss: " + e.Message + " // " + e.TargetSite);
			    	Thread.Sleep(conf.SleepOnFail * 1000);	
			}
		} while (conf.Loop);
	    } catch (Exception e) {
		if (!string.IsNullOrEmpty(e.Message))
			Console.WriteLine("Fatal error: " + e.Message);
		return;
	    }
	}
    }
}
