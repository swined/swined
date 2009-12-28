#ifndef _MYNICKHANDLER_H
#define	_MYNICKHANDLER_H

#include "IPeerEventHandler.h"
#include "PeerConnection.h"


class MyNickHandler : public virtual IPeerHandler {
public:
    MyNickHandler(IPeerEventHandler *handler, PeerConnection *conn) {
        this->conn = conn;
        this->handler = handler;
    }
    MyNickHandler(const MyNickHandler& orig) {
        throw Exception("suddenly MyNickHandler(&)");
    }
    virtual ~MyNickHandler() {
        throw Exception("suddenly ~MyNickHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly MyNickHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        if (data.find("$MyNick") != 0)
            return;
        conn->onPeerNickReceived(StringUtils::split(data, ' ', 2)[1]);
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _MYNICKHANDLER_H */

