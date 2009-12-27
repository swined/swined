#ifndef _IHUBEVENTHANDLER_H
#define	_IHUBEVENTHANDLER_H

#include "SearchResult.h"

class IHubEventHandler {
public:
    virtual void onHubConnected() = 0;
    virtual void onSearchResult(const SearchResult& result) = 0;
    virtual void onPeerConnectionRequested(const std::string& ip, int port) = 0;
    virtual ~IHubEventHandler() {};
private:

};

#endif	/* _IHUBEVENTHANDLER_H */

