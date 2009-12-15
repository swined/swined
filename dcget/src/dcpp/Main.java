package dcpp;

import hub.HubWriter;
import hub.HubReader;
import hub.HubConnection;
import java.net.Socket;
import logger.ConsoleLogger;

public class Main {

    public static void main(String[] args) throws Exception {
        ConsoleLogger l = new ConsoleLogger();
        HubWriter w = new HubWriter(s.getOutputStream(), l);
        HubReader r = new HubReader(s.getInputStream(), l);
        HubConnection m = new HubConnection(r, w, "jined", "WXCQCYNWND57VJIFKIN45H6WYRHGDRS5NMLIDVY");
        while (s.isConnected()) {
            m.run();
            Thread.sleep(100);
        }
    }

}
