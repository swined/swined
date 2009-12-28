#ifndef _PEERREADER_H
#define	_PEERREADER_H

#include "Socket.h"
#include "ILogger.h"
#include "IPeerHandler.h"
#include <vector>
#include "StringUtils.h"

class PeerReader {
public:
    
    PeerReader(const PeerReader& orig) {
        throw Exception("suddenly PeerReader(&)");
    }
    virtual ~PeerReader() {
        throw Exception("suddenly ~PeerReader()");
    }

    PeerReader(Socket *in, ILogger *logger) {
        this->in = in;
        this->logger = logger;
        this->expectData = 0;
    }

    void registerHandler(IPeerHandler *handler) {
        handlers.push_back(handler);
    }

    void expect(int len) {
        this->expectData = len;
    }

    void read() {
        readStream();
        if (expectData > 0) {
            parseData();
        } else {
            parseCommand();
        }
    }

private:

    ILogger *logger;
    Socket *in;
    std::string buffer;
    std::vector<IPeerHandler*> handlers;
    int expectData;

    void readStream() {
        std::string buf;
        in->recv(buf);
        if (buf.length() == 0)
            return;
        buffer.append(buf);
    }

    void parseCommand() {
        int ix = buffer.find('|', 0);
        if (ix != -1) {
            std::string cmd = buffer.substr(0, ix);
            buffer.replace(0, ix + 1, "");
            logger->debug(std::string("received peer command: ") + cmd);
            for (int i = 0; i < handlers.size(); i++)
                handlers.at(i)->handlePeerCommand(cmd);
        }
    }

    void parseData() {
        if (buffer.length() >= expectData) {
            std::string b = buffer.substr(0, expectData - 1);
            buffer.replace(0, expectData, "");
            for (int i = 0; i < handlers.size(); i++)
                handlers.at(i)->handlePeerData(b);
//        } else {
  //          if (buffer.length() > 10000)
    //            throw Exception(StringUtils::itoa(expectData - buffer.length()) + " bytes expected");
        }
    }


};

#endif	/* _PEERREADER_H */

