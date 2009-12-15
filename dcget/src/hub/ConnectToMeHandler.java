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
        handler.onPeerConnectionRequested(s.split(" ")[1]);
    }

}
