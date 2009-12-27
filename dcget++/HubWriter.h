#ifndef _HUBWRITER_H
#define	_HUBWRITER_H

#include "Socket.h"
#include "Exception.h"

class HubWriter {
public:
    HubWriter(const HubWriter& orig) {
        throw Exception("suddenly HubWriter(&)");
    }
    virtual ~HubWriter() {
        throw Exception("suddenly ~HubWriter()");
    }
    HubWriter(Socket *sock, ILogger *logger) {
        this->out = sock;
        this->logger = logger;
    }
    void sendValidateNick(const std::string& nick) {
        throw Exception("suddenly sendValidateNick()");
    }
    void sendKey(const std::string& key) {
        throw Exception("suddenly sendKey()");
    }
    void sendVersion(const std::string& version) {
        throw Exception("suddenly sendVersion()");
    }
    void sendMyInfo(const std::string& nick) {
        throw Exception("suddenly sendMyInfo()");
    }
    void sendTTHSearch(const std::string& nick, const std::string& tth) {
        throw Exception("suddenly sendTTHSearch()");
    }
    void sendRevConnectToMe(const std::string& nick, const std::string& target) {
        throw Exception("suddenly sendRevConnectToMe()");
    }

private:
    ILogger *logger;
    Socket *out;
    void sendString(std::string s);
};

#endif	/* _HUBWRITER_H */

