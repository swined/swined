#ifndef _HUBREADER_H
#define	_HUBREADER_H

#include "IHubHandler.h"
#include "Socket.h"
#include "Exception.h"
#include "ILogger.h"
#include <vector>

class HubReader {
public:
    HubReader() {

    }

    HubReader(const HubReader& orig) {
        throw Exception("suddenly HubReader(copy)");
    }
    virtual ~HubReader() {
        for (int i = 0; i < handlers.size(); i++)
            delete handlers[i];
    }
    HubReader(Socket sock, ILogger *logger) {
        this->in = sock;
        this->logger = logger;
    }
    void registerHandler(IHubHandler* handler) {
        handlers.push_back(handler);
    }
    void run() {
        readStream();
        parseCommand();
    }

private:

    ILogger *logger;
    Socket in;
    std::string buffer;
    std::vector<IHubHandler*> handlers;
    void readStream() {
        std::string buf;
        in.recv(buf);
        if (buf.length() == 0)
            return;
        buffer.append(buf);
    }
    void parseCommand() {
        int ix = buffer.find('|', 0);
        if (ix != -1) {
            std::string cmd = buffer.substr(0, ix);
            buffer.replace(0, ix + 1, "");
            logger->debug(std::string("received hub command: ") + cmd);
            for (int i = 0; i < handlers.size(); i++)
                handlers.at(i)->handleHubCommand(cmd);
        }
    }

};

#endif	/* _HUBREADER_H */

