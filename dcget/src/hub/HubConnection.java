package hub;

import java.net.Socket;
import logger.ILogger;
import util.KeyGenerator;

public class HubConnection {

    private IHubEventHandler handler;
    private HubReader reader;
    private HubWriter writer;
    private String nick;

    public HubConnection(IHubEventHandler handler, ILogger logger, String host, int port, String nick) throws Exception {
        this.handler = handler;
        logger.debug("connecting to " + host + ":" + port);
        Socket sock = new Socket(host, port);
        this.reader = new HubReader(sock.getInputStream(), logger);
        this.writer = new HubWriter(sock.getOutputStream(), logger);
        this.nick = nick;
        reader.registerHandler(new LockHandler(this));
        reader.registerHandler(new SRHandler(handler));
        reader.registerHandler(new ConnectToMeHandler(handler));
    }

    public void run() throws Exception {
        reader.read();
    }

    public void onHubConnected(String lock) throws Exception {
        writer.sendKey(KeyGenerator.generateKey(lock.getBytes()));
        writer.sendValidateNick(nick);
        writer.sendVersion("0.01");
        writer.sendMyInfo(nick);
        handler.onHubConnected();
    }

    public void search(String tth) throws Exception {
        writer.sendTTHSearch(nick, tth);
    }

    public void requestPeerConnection(String target) throws Exception {
        writer.sendRevConnectToMe(nick, target);
    }

}
