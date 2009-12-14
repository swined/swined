package dcpp;

public class LockHandler implements IHubHandler {

    private HubConnectionManager mgr;

    public LockHandler(HubConnectionManager mgr) {
        this.mgr = mgr;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String d = new String(data);
        if (!d.startsWith("$Lock ")) {
            return;
        }
        String[] s = d.split(" ");
        if (s.length < 2) {
            return;
        }
        mgr.onHubConnected(s[1]);
    }

}
