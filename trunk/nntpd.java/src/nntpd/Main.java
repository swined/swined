package nntpd;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket sock = new ServerSocket(1119);
            while (true) {
                System.out.println("waiting for connection");
                new Thread(new ClientWorker(sock.accept())).start();
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

}
