#ifndef _BYTEARRAY_H
#define	_BYTEARRAY_H

#include <cstring>
#include <vector>
#include "Exception.h"

class ByteArray {
public:
    ByteArray() {
        length = 0;
        data = (char*)malloc(length + 1);
        data[0] = 0;
    }

    ByteArray(const ByteArray& orig) {
        throw Exception("suddenly copy constructor");
    }

    bool startsWith(const char *data) const {
        if (strlen(data) > length)
            return false;
        for (int i = 0; i < strlen(data); i++)
            if (data[i] != this->data[i])
                return false;
        return true;
    }

    int indexOf(char c) {
        return indexOf(c, 0);
    }

    int indexOf(char c, int start) {
        for (int i = start; i < length; i++)
            if (data[i] == c)
                return i;
        return -1;
    }

    std::vector<ByteArray*> *split(char b) const {
        return split(b, 0);
    }

    std::vector<ByteArray*> *split(char b, int c) const {
        throw std::exception();
    }

    std::string toString() {
        return std::string("xxx");
    }

    int toInt() {
        return atoi(data);
    }

    virtual ~ByteArray() {}
private:

    int length;
    char *data;
};

#endif	/* _BYTEARRAY_H */

