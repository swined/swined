package hub;

public interface IHubEventHandler {

    void onHubConnected() throws Exception;
    void onSearchResult(SearchResult result) throws Exception;
    void onPeerConnectionRequested(String ip) throws Exception;

}
