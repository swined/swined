#ifndef _PEERLOGGER_H
#define	_PEERLOGGER_H

class PeerLogger : public virtual ILogger {
public:
    PeerLogger(const PeerLogger& orig) {
        throw Exception("suddenly PeerLogger(&)");
    }

    virtual ~PeerLogger() {
        
    }

    PeerLogger(ILogger *logger, const std::string& name) {
        this->logger = logger;
        this->name = name;
    }

    void debug(const std::string& msg) {
        logger->debug("["+ name + "]: " + msg);
    }

    void info(const std::string& msg) {
        logger->info("["+ name + "]: " + msg);
    }
    void warn(const std::string& msg) {
        logger->warn("["+ name + "]: " + msg);
    }
    void error(const std::string& msg) {
        logger->error("["+ name + "]: " + msg);
    }

private:

    ILogger *logger;
    std::string name;

};

#endif	/* _PEERLOGGER_H */

