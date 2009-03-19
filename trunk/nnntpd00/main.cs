using System;
using System.IO;
using System.Text;
using System.Net.Sockets;
using System.Threading;

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

}

class NntpClient {

	private NntpStreamReader reader;
	private NntpStreamWriter writer;

	public NntpClient(NntpStreamReader reader, NntpStreamWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	public void Run() {
		string cmd;
		writer.WriteNntpResponse(200, "server ready - posting allowed");
		while (null != (cmd = reader.ReadNntpCommand())) {
			Console.WriteLine(cmd);
		}
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
							NntpClient nntpClient = new NntpClient(reader, writer);
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
