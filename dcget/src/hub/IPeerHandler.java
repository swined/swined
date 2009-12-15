package hub;

public interface IPeerHandler {

    public void handlePeerCommand(byte[] data) throws Exception;
    public void handlePeerData(byte[] data) throws Exception;

}
