package peer;

public interface IPeerEventHandler {

    void onPeerConnected(PeerConnection peer) throws Exception;
    void onFileLengthReceived(PeerConnection peer, int length) throws Exception;
    void onHandShakeDone(PeerConnection peer) throws Exception;
    void onNoFreeSlots(PeerConnection peer) throws Exception;
    void onPeerError(PeerConnection peer, String error) throws Exception;
    void onPeerData(PeerConnection peer, byte[] data) throws Exception;
    
}
