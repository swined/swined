#ifndef _EXCEPTION_H
#define	_EXCEPTION_H

#include <stdlib.h>
#include <string.h>

class Exception {
public:
    Exception(const char *message) {
        this->message = (char*)malloc(strlen(message)+1);
        strcpy(this->message, message);
    }
    Exception(const Exception& orig) {
        this->message = (char*)malloc(strlen(orig.message)+1);
        strcpy(this->message, orig.message);
    }
    char *getMessage() {
        return message;
    }
    virtual ~Exception() throw() {
        delete message;
    }

private:

    char *message;

};

#endif	/* _EXCEPTION_H */

