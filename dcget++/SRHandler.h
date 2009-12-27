#ifndef _SRHANDLER_H
#define	_SRHANDLER_H

class SRHandler : public virtual IHubHandler {
public:
    SRHandler(const SRHandler& orig) {
        throw Exception("suddenly SRHandler()");
    }
    virtual ~SRHandler() {
        throw Exception("suddenly ~SRHandler()");
    }
    SRHandler(IHubEventHandler *handler) {
        this->handler = handler;
    }
    void handleHubCommand(const std::string& data) {
        throw Exception("suddenly handleHubCommand()");
    }

private:

        IHubEventHandler *handler;

};

#endif	/* _SRHANDLER_H */

