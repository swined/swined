#include <stdlib.h>

#include "DownloadManager.h"
#include "ConsoleLogger.h"
#include <fstream>

int main(int argc, char** argv) {
    ConsoleLogger logger;
    int r = 0;
    try {
        if (argc != 5)
            throw Exception("expected exactly 4 args");
        std::ofstream out(argv[4], std::ios::out | std::ios::binary);
        if(!out)
            throw Exception("failed to open file");
        DownloadManager dm(&logger, &out);
        dm.download(std::string(argv[1]), atoi(argv[2]), std::string(argv[3]));
        out.close();
        logger.info("done");
    } catch (Exception e) {
        logger.error(e.getMessage());
        r = 1;
    }
    return r;
}

