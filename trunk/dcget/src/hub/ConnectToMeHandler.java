package hub;

public class ConnectToMeHandler implements IHubHandler {

    private HubConnection mgr;

    public ConnectToMeHandler(HubConnection mgr) {
        this.mgr = mgr;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$ConnectToMe"))
            return;
        mgr.onPeerConnectionRequested(s.split(" ")[1]);
    }

}
