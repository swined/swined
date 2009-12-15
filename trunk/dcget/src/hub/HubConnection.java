package hub;

import util.KeyGenerator;

public class HubConnection {

    private IHubEventHandler handler;
    private HubReader reader;
    private HubWriter writer;
    private String nick;

    public HubConnection(IHubEventHandler handler, HubReader reader, HubWriter writer, String nick) {
        this.handler = handler;
        this.reader = reader;
        this.writer = writer;
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
}
