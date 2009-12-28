#include <stdlib.h>

#include "DownloadManager.h"
#include "ConsoleLogger.h"

int main(int argc, char** argv) {
    ConsoleLogger logger;
    DownloadManager dm(&logger);
    try {
        dm.download(std::string("89.31.118.42"), 411, std::string("STMUNEWY73LI5KQCVMLWXDMGXZKD76GPJ3M6EQA"));
    } catch (Exception e) {
        logger.error(e.getMessage());
    }
    return (EXIT_SUCCESS);
}

