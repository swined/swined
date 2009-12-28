#ifndef _ERRORHANDLER_H
#define	_ERRORHANDLER_H

class ErrorHandler : public virtual IPeerHandler {
public:
    ErrorHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        throw Exception("suddenly ErrorHandler()");
    }
    ErrorHandler(const ErrorHandler& orig) {
        throw Exception("suddenly ErrorHandler(&)");
    }
    virtual ~ErrorHandler() {
        throw Exception("suddenly ~ErrorHandler()");
    }

    void handlePeerData(const std::string& data) {
        throw Exception("suddenly ErrorHandler::handlePeerData()");
    }

    void handlePeerCommand(const std::string& data) {
        throw Exception("suddenly ErrorHandler::handlePeerCommand()");
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _ERRORHANDLER_H */

