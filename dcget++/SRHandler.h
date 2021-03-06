#ifndef _SRHANDLER_H
#define	_SRHANDLER_H

#include "IHubEventHandler.h"

class SRHandler : public virtual IHubHandler {
public:
    SRHandler(const SRHandler& orig) {
        throw Exception("suddenly SRHandler(&)");
    }
    virtual ~SRHandler() {
    }
    SRHandler(IHubEventHandler *handler) {
        this->handler = handler;
    }
    void handleHubCommand(const std::string& data) {
        if (data.find("$SR") != 0)
            return;
        std::vector<std::string> d = StringUtils::split(data, ' ', 3);
        std::vector<std::string> r = StringUtils::split(d[2], 0x05, 3);
        std::string info = StringUtils::split(r[1], ' ', 2)[1];
        std::vector<std::string> slots = StringUtils::split(info, '/', 2);
        SearchResult sr(d[1], r[0], atoi(slots[0].c_str()), atoi(slots[1].c_str()));
        handler->onSearchResult(sr);
    }

private:

        IHubEventHandler *handler;

};

#endif	/* _SRHANDLER_H */

