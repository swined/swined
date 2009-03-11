using System;
using System.Net.Sockets;

class MainClass {

	public static void Main() {	
		TcpListener server = null;
		try {
			server = new TcpListener(1119);
			server.Start();
			while (true) {
				Console.WriteLine("waiting for connection");
				TcpClient client = server.AcceptTcpClient();
				Console.WriteLine("connected");
				client.Close();
			}
		} finally {
			server.Stop();
		}
	}

}
