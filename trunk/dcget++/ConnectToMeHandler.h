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
//        std::vector<ByteArray*> *t = data->split(' ');
//        ByteArray *addr = t->at(2);
//        std::vector<ByteArray*> *ip = addr->split(':', 2);
//        handler->onPeerConnectionRequested(ip->at(0)->toString(), ip->at(1)->toInt());
    }

    virtual ~ConnectToMeHandler() {
    };
private:

    IHubEventHandler *handler;

};

#endif	/* _CONNECTTOMEHANDLER_H */

