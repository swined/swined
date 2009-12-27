#ifndef _IHUBHANDLER_H
#define	_IHUBHANDLER_H

class IHubHandler {
public:
    virtual void handleHubCommand(const std::string& data) = 0;
    virtual ~IHubHandler() {};
private:

};

#endif	/* _IHUBHANDLER_H */

