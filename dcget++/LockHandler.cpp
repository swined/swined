#include "LockHandler.h"

void LockHandler::handleHubCommand(const std::string& data) {
    if (data.find("$Lock") != 0)
        return;
    std::vector<std::string> s = StringUtils::split(data, ' ');
    if (s.size() < 2)
        throw Exception("lock not found");
    mgr->onHubConnected(s[1]);
}

