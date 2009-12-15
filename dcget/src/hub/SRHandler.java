package hub;

public class SRHandler implements IHubHandler {

    private IHubEventHandler handler;

    public SRHandler(IHubEventHandler handler) {
        this.handler = handler;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$SR"))
            return;
        String nick = s.split(" ")[1];
        String file = s.split(" ", 3)[2].split(new String(new byte[] { 0x05 }))[0];
        handler.onSearchResult(new SearchResult(nick, file));
    }

}
