package dcpp;

public interface IHubHandler {

    void handleHubCommand(byte[] data) throws Exception;

}
