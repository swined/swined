package hub;

public interface IHubEventHandler {

    void onSearchResult(HubConnection hub, SearchResult result) throws Exception;

}
