#ifndef _SRHANDLER_H
#define	_SRHANDLER_H

#include "IHubEventHandler.h"

class SRHandler : public virtual IHubHandler {
public:
    SRHandler(const SRHandler& orig) {
        throw Exception("suddenly SRHandler()");
    }
    virtual ~SRHandler() {
    }
    SRHandler(IHubEventHandler *handler) {
        this->handler = handler;
    }
    void handleHubCommand(const std::string& data) {
        if (data.find("$SR") != 0)
            return;
        throw Exception("suddenly SRHandler::handle()");
    }

private:

        IHubEventHandler *handler;

};

#endif	/* _SRHANDLER_H */

