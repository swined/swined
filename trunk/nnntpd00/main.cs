using System;
using System.Net.Sockets;
using System.Threading;

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
					client.Close();
				}).Start();
			}
		} finally {
			server.Stop();
		}
	}

}
