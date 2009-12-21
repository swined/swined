package hub;

import util.ArrayUtils;

class SRHandler implements IHubHandler {

    private IHubEventHandler handler;

    public SRHandler(IHubEventHandler handler) {
        this.handler = handler;
    }

    public void handleHubCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$SR"))
            return;
        byte[][] d = ArrayUtils.split(data, (byte)0x20, 3);
        byte[][] r = ArrayUtils.split(d[2], (byte)0x05);
        String info = new String(r[1]).split(" ", 2)[1];
        String[] slots = info.split("/", 2);
        handler.onSearchResult(new SearchResult(new String(d[1]), r[0], new Integer(slots[0]), new Integer(slots[1])));
    }

}
