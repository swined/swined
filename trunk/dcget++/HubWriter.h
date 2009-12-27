#ifndef _HUBWRITER_H
#define	_HUBWRITER_H

#include "Socket.h"
#include "Exception.h"

class HubWriter {
public:
    HubWriter() {
        
    }
    HubWriter(const HubWriter& orig) {
        throw Exception("suddenly HubWriter(&)");
    }
    virtual ~HubWriter() {
    }
    HubWriter(Socket sock, ILogger *logger) {
        this->out = sock;
        this->logger = logger;
    }
    void sendValidateNick(const std::string& nick) {
        sendCommand(std::string("$ValidateNick ") + nick);
    }
    void sendKey(const std::string& key) {
        sendCommand(std::string("$Key ") + key);
    }
    void sendVersion(const std::string& version) {
        sendCommand(std::string("$Version ") + version);
    }
    void sendMyInfo(const std::string& nick) {
        sendCommand(std::string("$MyINFO $ALL ") + nick + std::string(" $ $56k\x08$$1000000000000$"));
    }
    void sendTTHSearch(const std::string& nick, const std::string& tth) {
        sendCommand(std::string("$Search Hub:") + nick + std::string(" F?F?0?9?TTH:") + tth);
    }
    void sendRevConnectToMe(const std::string& nick, const std::string& target) {
        throw Exception("suddenly sendRevConnectToMe()");
    }

private:
    ILogger *logger;
    Socket out;
    void sendCommand(std::string s) {
        logger->debug(std::string("send command to hub: ") + s);
        out.send(s + std::string("|"));
    }
};

#endif	/* _HUBWRITER_H */

