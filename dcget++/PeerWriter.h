#ifndef _PEERWRITER_H
#define	_PEERWRITER_H

#include "Socket.h"
#include "StringUtils.h"

class PeerWriter {
public:
    PeerWriter(const PeerWriter& orig) {
        throw Exception("suddenly PeerWriter(&)");
    }
    virtual ~PeerWriter() {
        
    }

    PeerWriter(Socket *out, ILogger *logger) {
        this->out = out;
        this->logger = logger;
    }

    void sendMyNick(const std::string& nick) {
        sendString("$MyNick " + nick + "|");
    }

    void sendLock(const std::string& lock, const std::string& pk) {
        sendString("$Lock " + lock + " Pk=" + pk + "|");
    }

    void sendGet(const std::string& file, int start) {
        sendString("$Get " + file + "$" + StringUtils::itoa(start) + "|");
    }

    void sendKey(const std::string& key) {
        sendString("$Key " + key + "|");
    }

    void sendDirection(const std::string& direction, int a) {
        sendString("$Direction " + direction + " " + StringUtils::itoa(a) + "|");
    }

    void sendSend() {
        sendString("$Send|");
    }

private:

    ILogger *logger;
    Socket *out;

    void sendString(const std::string& s) {
        logger->debug("sent string to peer: " + s);
        out->send(s);
    }


};

#endif	/* _PEERWRITER_H */

