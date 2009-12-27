#ifndef _SEARCHRESULT_H
#define	_SEARCHRESULT_H

#include <string>

#include "ByteArray.h"

class SearchResult {
public:

    SearchResult(const std::string& nick, const ByteArray& file, int freeSlots, int totalSlots) {
        this->nick = nick;
        this->file = file;
        this->freeSlots = freeSlots;
        this->totalSlots = totalSlots;
    }
    SearchResult(const SearchResult& orig) {
        this->nick = orig.nick;
        this->file = orig.file;
        this->freeSlots = orig.freeSlots;
        this->totalSlots = orig.totalSlots;

    }
    virtual ~SearchResult() {}
private:

    std::string nick;
    ByteArray file;
    int freeSlots;
    int totalSlots;

};

#endif	/* _SEARCHRESULT_H */

