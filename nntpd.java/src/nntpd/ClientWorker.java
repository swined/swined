package nntpd;

import java.io.IOException;
import java.net.Socket;

public class ClientWorker implements Runnable {

    Socket client;

    public ClientWorker(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            System.out.printf(
                "client connected %s\n",
                client.getRemoteSocketAddress().toString()
            );
            new NntpClient(
                new FakeNntpDataProvider(),
                new NntpStreamReader(client.getInputStream()),
                new NntpStreamWriter(client.getOutputStream())
            ).run();
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

}
