#ifndef _MESSAGEBUS_H
#define	_MESSAGEBUS_H

#include "Message.h"
#include "MessageHandler.h"

class MessageBus {
public:
    MessageBus();
    template<Message M> void subscribe<M>(MessageHandler<M>*);
private:

};

#endif	/* _MESSAGEBUS_H */

