#ifndef _CONNECTTOMEHANDLER_H
#define	_CONNECTTOMEHANDLER_H

#include "IHubEventHandler.h"

class ConnectToMeHandler : public virtual IHubHandler {
public:

    ConnectToMeHandler(IHubEventHandler *handler) {
        this->handler = handler;
    }

    ConnectToMeHandler(const ConnectToMeHandler& orig) {
        this->handler = orig.handler;
    }

    virtual void handleHubCommand(const std::string& data) {
        if (data.find("$ConnectToMe") != 0)
            return;
        throw Exception("suddenly ConnectoToMeHandler::handle()");
    }

    virtual ~ConnectToMeHandler() {
    };
private:

    IHubEventHandler *handler;

};

#endif	/* _CONNECTTOMEHANDLER_H */

