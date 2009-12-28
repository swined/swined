#ifndef _IPEERHANDLER_H
#define	_IPEERHANDLER_H

class IPeerHandler {
public:
    virtual void handlePeerCommand(const std::string& data) = 0;
    virtual void handlePeerData(const std::string& data) = 0;
    virtual ~IPeerHandler() {};
private:

};

#endif	/* _IPEERHANDLER_H */

