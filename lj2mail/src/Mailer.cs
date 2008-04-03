// $Id: Mailer.cs 213 2007-09-14 09:43:07Z swined $

using System;
using System.Web.Mail;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections.Specialized;
using System.Reflection;

    public class Mailer {
	public class Temp {
	    public string Journal;
	}
	protected Config conf;
	
	public Mailer(Config conf) {
	    this.conf = conf;	    
	}
	
	protected string base64(string text) {
	    string res = "";
	    text = Convert.ToBase64String(Encoding.UTF8.GetBytes(text));	
	    for (int i = 0; i < text.Length; i++)
		res += text[i] + (i % 64 == 63 ? "\n" : "");
	    return res;
	}
	
	protected void sendMail(string text, string subject) {
#pragma warning disable 0618
	    MailMessage msg = new MailMessage();
	    msg.From = conf.MailFrom;
	    msg.To = conf.MailTo;
	    msg.Subject = subject;
	    if (conf.UseBase64) {
		msg.Body = base64(text);
		msg.Headers.Add("Content-Transfer-Encoding", "base64");	    
	    } else
		msg.Body = text;
	    msg.BodyFormat = MailFormat.Html;
	    msg.BodyEncoding = Encoding.UTF8;
	    SmtpMail.Send(msg);
#pragma warning restore 0618	    
	}
	
	protected void sendMail(string journal, RssChannel channel, RssItem item) {
	    Temp tm = new Temp();
	    Formatter fm = new Formatter();
	    fm.Objects.Add("channel", channel);
	    fm.Objects.Add("entry", item);
	    fm.Objects.Add("conf", conf);
	    fm.Objects.Add("this", tm);
	    tm.Journal = journal;
	    sendMail(fm.Format(conf.MessageFormat), fm.Format(conf.SubjectFormat));
	}
	
	public void SendMail(string journal, RssFeed feed) {
	    foreach (RssItem item in feed.Channel.Items) 
		sendMail(journal, feed.Channel, item);
	}
    }
