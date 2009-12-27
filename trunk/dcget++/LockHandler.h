#ifndef _LOCKHANDLER_H
#define	_LOCKHANDLER_H

#include "HubConnection.h"

class HubConnection;
class LockHandler : public virtual IHubHandler {
public:
    LockHandler(const LockHandler& orig) {
        throw Exception("suddenly LockHandler(&)");
    }
    virtual ~LockHandler() {
        throw Exception("suddenly ~LockHandler()");
    }
    LockHandler(HubConnection *mgr) {
        this->mgr = mgr;
    }
    void handleHubCommand(const std::string& data) {
        throw Exception("suddenly handleHubCommand()");
    }

private:

    HubConnection *mgr;

};

#endif	/* _LOCKHANDLER_H */

