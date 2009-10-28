#ifndef _MESSAGEHANDLER_H
#define	_MESSAGEHANDLER_H

template<Message M> class MessageHandler<M> {
public:
    void handleMessage(const M&) = 0;
};

#endif	/* _MESSAGEHANDLER_H */

