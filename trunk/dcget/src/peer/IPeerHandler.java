package peer;

interface IPeerHandler {

    public void handlePeerCommand(byte[] data);
    public void handlePeerData(byte[] data);

}
