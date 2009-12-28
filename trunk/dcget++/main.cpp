#include <stdlib.h>

#include "DownloadManager.h"
#include "ConsoleLogger.h"
#include <fstream>

int main(int argc, char** argv) {
    ConsoleLogger logger;
    try {
        std::ofstream out("/home/sw/dcget.mp3", std::ios::out | std::ios::binary);
        if(!out)
            throw Exception("failed to open file");
        DownloadManager dm(&logger, &out);
        dm.download(std::string("89.31.118.42"), 411, std::string("STMUNEWY73LI5KQCVMLWXDMGXZKD76GPJ3M6EQA"));
        out.close();
        logger.info("done");
    } catch (Exception e) {
        logger.error(e.getMessage());
    }
    return (EXIT_SUCCESS);
}

