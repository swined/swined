#ifndef _HUBREADER_H
#define	_HUBREADER_H

#include "IHubHandler.h"
#include "Socket.h"

class HubReader {
public:
    HubReader(const HubReader& orig) {
        throw Exception("suddenly HubReader(copy)");
    }
    virtual ~HubReader() {
        for (int i = 0; i < handlers.size(); i++)
            delete handlers.at(i);
    }
    HubReader(Socket *sock, ILogger *logger) {
        this->in = sock;
        this->logger = logger;
        buffer = std::string();
        handlers = std::vector<IHubHandler*>();
    }
    void registerHandler(IHubHandler *handler) {
        handlers.push_back(handler);
    }
    void run() {
        readStream();
        parseCommand();
    }

private:

    ILogger *logger;
    Socket *in;
    std::string buffer;
    std::vector<IHubHandler*> handlers;
    void readStream() {
        std::string buf = std::string();
        in->recv(buf);
        if (buf.length() == 0);
            return;
        buffer.append(buf);
    }
    void parseCommand() {
        int ix = buffer.find('|', 0);
        if (ix != -1) {
            std::string cmd = buffer.substr(0, ix - 1);
            buffer.replace(0, ix, "");
            logger->debug(std::string("received hub command: ") + cmd);
            for (int i = 0; i < handlers.size(); i++)
                handlers.at(i)->handleHubCommand(cmd);
        }
    }

};

#endif	/* _HUBREADER_H */

