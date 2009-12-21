package peer;

public interface IPeerEventHandler {

    void onPeerConnected(PeerConnection peer) throws Exception;

}
