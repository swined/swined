#ifndef _LOCKHANDLER_H
#define	_LOCKHANDLER_H

#include "HubConnection.h"
#include "StringUtils.h"

class HubConnection;
class LockHandler : public virtual IHubHandler {
public:
    LockHandler(const LockHandler& orig) {
        throw Exception("suddenly LockHandler(&)");
    }
    virtual ~LockHandler() {
    }
    LockHandler(HubConnection *mgr) {
        this->mgr = mgr;
    }
    void handleHubCommand(const std::string& data);

private:

    HubConnection *mgr;

};

#endif	/* _LOCKHANDLER_H */

