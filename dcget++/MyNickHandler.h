#ifndef _MYNICKHANDLER_H
#define	_MYNICKHANDLER_H

#include "IPeerEventHandler.h"
#include "PeerConnection.h"


class MyNickHandler : public virtual IPeerHandler {
public:
    MyNickHandler(IPeerEventHandler *handler, PeerConnection *conn) {
        throw Exception("suddenly MyNickHandler()");
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
        throw Exception("suddenly MyNickHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _MYNICKHANDLER_H */

