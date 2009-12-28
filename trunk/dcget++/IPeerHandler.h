#ifndef _IPEERHANDLER_H
#define	_IPEERHANDLER_H

class IPeerHandler {
public:
    void handlePeerCommand(const std::string& data);
    void handlePeerData(const std::string& data);
    virtual ~IPeerHandler() {};
private:

};

#endif	/* _IPEERHANDLER_H */

