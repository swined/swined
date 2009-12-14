package dcpp;

import java.net.Socket;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("p2p.academ.org", 411);
        ConsoleLogger l = new ConsoleLogger();
        HubWriter w = new HubWriter(s.getOutputStream(), l);
        HubReader r = new HubReader(s.getInputStream(), l);
        DownloadManager m = new DownloadManager(r, w, "jined", "WXCQCYNWND57VJIFKIN45H6WYRHGDRS5NMLIDVY");
        while (s.isConnected()) {
            m.run();
            Thread.sleep(100);
        }
    }

}
