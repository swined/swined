package peer;

public interface IPeerEventHandler {

    void onPeerConnected(PeerConnection peer) throws Exception;
    void onFileLengthReceived(PeerConnection peer, int length) throws Exception;
    void onHandShakeDone(PeerConnection peer) throws Exception;

}
