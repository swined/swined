using System;
using System.IO;
using System.Text;
using System.Linq;
using System.Net.Sockets;
using System.Threading;
using System.Collections.Generic;

interface INntpDataProvider {
	List<string> Groups();
	string GetName(string group);
	int GetLastId(string group);
	int GetFirstId(string group);
	int EstimateCount(string group);
	bool PostingAllowed(string group);
}

class TestNntpDataProvider : INntpDataProvider {

	public List<string> Groups() {
		return (new string[] {"test.group1", "test.group2", "test.group3.subgroup"}).ToList();
	}

	public string GetName(string group) {
		return group;
	}

	public int GetLastId(string group) {
		return 2;
	}

	public int GetFirstId(string group) {
		return 1;
	}

	public int EstimateCount(string group) {
		return 2;
	}

	public bool PostingAllowed(string group) {
		return false;
	}

}

class NntpStreamReader : StreamReader {

	public NntpStreamReader(Stream stream) : base(stream) {
	}

	public string ReadNntpText() {
		StringBuilder sb = new StringBuilder();
		string line;
		while (null != (line = this.ReadLine())) {
			if ("." == line)
				break;
			sb.AppendLine(line);
		}
		return sb.ToString();
	}

	public string ReadNntpCommand() {
		return this.ReadLine();
	}

}

class NntpStreamWriter : StreamWriter {
	
	public NntpStreamWriter(Stream stream) : base(stream) {
	}

	public void WriteNntpResponse(int code, string response) {
		this.WriteLine(code.ToString() + " " + response);
		this.Flush();
	}

	public void WriteNntpTextLine(string line) {
		if ('.' == line[0])
			this.Write(".");
		this.WriteLine(line);
		this.Flush();
	}

	public void WriteNntpTextEnd() {
		this.WriteLine(".");
		this.Flush();
	}

}

class NntpClient {

	private string currentGroup;
	private NntpStreamReader reader;
	private NntpStreamWriter writer;
	private INntpDataProvider dataProvider;

	public NntpClient(INntpDataProvider dataProvider, NntpStreamReader reader, NntpStreamWriter writer) {
		this.dataProvider = dataProvider;
		this.reader = reader;
		this.writer = writer;
	}

	public void SendGreeting(bool postingAllowed, string text) {
		writer.WriteNntpResponse(postingAllowed ? 200 : 201, text);
	}

	public void Run() {
		string cmd;
		this.SendGreeting(false, "server ready - posting not allowed");
		while (null != (cmd = reader.ReadNntpCommand())) {
			string[] c = cmd.Split(" ".ToCharArray(), 2);
			string cn = c[0];
			string cp = 2 == c.Length ? c[1] : null;
			switch (cn.ToUpper()) {
				case "MODE": this.MODE(cp); break;
				case "LIST": this.LIST(cp); break;
				case "GROUP": this.GROUP(cp); break;
				default: writer.WriteNntpResponse(500, "unknown command"); break;
			}
		}
	}

	public void MODE(string mode) {
		if ("READER" == mode.ToUpper()) {
			this.SendGreeting(false, "server ready - posting not allowed");
			return;
		}
		writer.WriteNntpResponse(501, "unknown mode");
	}

	public void LIST(string param) {
		writer.WriteNntpResponse(215, "list of newsgroups follows");
		foreach (string group in dataProvider.Groups()) {
			StringBuilder sb = new StringBuilder();
			sb.AppendFormat(
				"{0} {1} {2} {3}",
				group,
				dataProvider.GetLastId(group),
				dataProvider.GetFirstId(group),
				dataProvider.PostingAllowed(group) ? "y" : "n"
			);
			writer.WriteNntpTextLine(sb.ToString());
		}
		writer.WriteNntpTextEnd();
	}

	public void GROUP(string param) {
		if (!dataProvider.Groups().Contains(param)) {
			writer.WriteNntpResponse(411, "no such news group");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.AppendFormat(
			"{0} {1} {2} {3} group selected",
			dataProvider.EstimateCount(param),
			dataProvider.GetFirstId(param),
			dataProvider.GetLastId(param),
			dataProvider.GetName(param)
		);
		writer.WriteNntpResponse(211, sb.ToString());
	}

}

class MainClass {

	public static void Main() {	
		TcpListener server = null;
		try {
			server = new TcpListener(1119);
			server.Start();
			while (true) {
				Console.WriteLine("waiting for connection");
				TcpClient client = server.AcceptTcpClient();
				new Thread(delegate() {
					Console.WriteLine("connected");
					using (NntpStreamReader reader = new NntpStreamReader(client.GetStream())) {
						using (NntpStreamWriter writer = new NntpStreamWriter(client.GetStream())) {
							INntpDataProvider dataProvider = new TestNntpDataProvider();
							NntpClient nntpClient = new NntpClient(dataProvider, reader, writer);
							nntpClient.Run();
						}
					}
					client.Close();
					Console.WriteLine("connection closed");
				}).Start();
			}
		} finally {
			server.Stop();
		}
	}

}
