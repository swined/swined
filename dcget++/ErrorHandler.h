#ifndef _ERRORHANDLER_H
#define	_ERRORHANDLER_H

class ErrorHandler : public virtual IPeerHandler {
public:
    ErrorHandler(PeerConnection *conn, IPeerEventHandler *handler) {
        this->conn = conn;
        this->handler = handler;
    }
    ErrorHandler(const ErrorHandler& orig) {
        throw Exception("suddenly ErrorHandler(&)");
    }
    virtual ~ErrorHandler() {
        
    }

    void handlePeerData(const std::string& data) {
    }

    void handlePeerCommand(const std::string& data) {
        if (data.find("$Error") != 0)
            return;
        throw Exception(StringUtils::split(data, ' ', 2)[1]);
    }

private:

    PeerConnection *conn;
    IPeerEventHandler *handler;

};

#endif	/* _ERRORHANDLER_H */

