package peer;

public interface IPeerEventHandler {

    void onPeerConnected(PeerConnection peer) throws Exception;
    void onPeerNickReceived(PeerConnection peer, String nick) throws Exception;
    void onFileLengthReceived(PeerConnection peer, int length) throws Exception;

}
