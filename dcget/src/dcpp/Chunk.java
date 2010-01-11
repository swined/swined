package dcpp;

import peer.PeerConnection;

public class Chunk {

    private byte[] data;
    private int from;
    private int len;
    private PeerConnection peer;

    public Chunk(PeerConnection peer, int from, int len) {
        this.peer = peer;
        this.from = from;
        this.len = len;
        this.data = null;
    }

    public PeerConnection getPeer() {
        return peer;
    }

    public byte[] getData() {
        return data;
    }

    public int getStart() {
        return from;
    }

    public int getLength() {
        return len;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}
