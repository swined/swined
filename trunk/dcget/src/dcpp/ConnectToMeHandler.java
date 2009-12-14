package dcpp;

public class ConnectToMeHandler implements IHubHandler {

    private DownloadManager mgr;

    public ConnectToMeHandler(DownloadManager mgr) {
        this.mgr = mgr;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$ConnectToMe"))
            return;
        mgr.onPeerConnectionRequested(s.split(" ")[1]);
    }

}
