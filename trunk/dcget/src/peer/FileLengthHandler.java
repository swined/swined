package peer;

class FileLengthHandler implements IPeerHandler {

    private PeerConnection conn;
    private IPeerEventHandler handler;

    public FileLengthHandler(IPeerEventHandler handler, PeerConnection conn) {
        this.handler = handler;
        this.conn = conn;
    }

    public void handlePeerData(byte[] data) {

    }

    public void handlePeerCommand(byte[] data) throws Exception {
        String s = new String(data);
        if (!s.startsWith("$FileLength "))
            return;
        //handler.onFileLengthReceived(conn, new Integer(s.split(" ")[1]));
    }

}
