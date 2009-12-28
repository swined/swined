#ifndef _IPEEREVENTHANDLER_H
#define	_IPEEREVENTHANDLER_H

#include "PeerConnection.h"

class PeerConnection;
class IPeerEventHandler {
public:
    virtual void onPeerConnected(PeerConnection *peer) = 0;
    virtual void onFileLengthReceived(PeerConnection *peer, int length) = 0;
    virtual void onHandShakeDone(PeerConnection *peer) = 0;
    virtual void onNoFreeSlots(PeerConnection *peer) = 0;
    virtual void onPeerError(PeerConnection *peer, const std::string& error) = 0;
    virtual void onPeerData(PeerConnection *peer, const std::string& data) = 0;
    virtual ~IPeerEventHandler() {};
private:

};

#endif	/* _IPEEREVENTHANDLER_H */

