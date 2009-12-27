#include <stdlib.h>

#include "DownloadManager.h"
#include "ConsoleLogger.h"

int main(int argc, char** argv) {
    ConsoleLogger *logger = new ConsoleLogger();
    try {
        DownloadManager dm(logger);
        dm.download(std::string("89.31.118.42"), 411, std::string("W3DVLEHUKUTUVQEUG3A56M7VLNMZGSITPTFTXAI"));
    } catch (Exception e) {
        logger->error(e.getMessage());
    }
    return (EXIT_SUCCESS);
}

