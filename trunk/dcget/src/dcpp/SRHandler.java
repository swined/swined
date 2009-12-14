package dcpp;


public class SRHandler implements IHubHandler {

    private HubConnectionManager mgr;

    public SRHandler(HubConnectionManager mgr) {
        this.mgr = mgr;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$SR"))
            return;
        String nick = s.split(" ")[1];
        String file = s.split(" ", 3)[2].split(new String(new byte[] { 0x05 }))[0];
        mgr.onPeerFound(new PeerInfo(nick, file));
    }

}
