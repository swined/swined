using System;
using System.IO;
using System.Net.Sockets;

class MainClass {

	public static void Main() {	
		TcpListener server = new TcpListener(1119);
		server.Start();
		TcpClient client = server.AcceptTcpClient();
		StreamWriter writer = new StreamWriter(client.GetStream());
		StreamReader reader = new StreamReader(client.GetStream());
		Console.WriteLine("client connected");
		writer.WriteLine("201 fake nntp server - no posting allowed");
		writer.Flush();
		string line;
		while (null != (line = reader.ReadLine())) {
			Console.WriteLine(line);
		}
	}

}
