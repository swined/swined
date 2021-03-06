#ifndef _EXCEPTION_H
#define	_EXCEPTION_H

#include <stdlib.h>
#include <string.h>
#include <sstream>

class Exception {
public:
    Exception(const std::string& message) : message(message) {
    }
    Exception(const Exception& orig) : message(orig.message) {
    }
    std::string getMessage() {
        return message;
    }
    virtual ~Exception() {}

private:

    std::string message;

};

#endif	/* _EXCEPTION_H */

