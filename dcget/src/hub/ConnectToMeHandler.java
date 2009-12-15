package hub;

class ConnectToMeHandler implements IHubHandler {

    private IHubEventHandler handler;

    public ConnectToMeHandler(IHubEventHandler handler) {
        this.handler = handler;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$ConnectToMe"))
            return;
        String addr = s.split(" ")[2];
        String[] ip = addr.split(":");
        handler.onPeerConnectionRequested(ip[0], Integer.parseInt(ip[1]));
    }

}
