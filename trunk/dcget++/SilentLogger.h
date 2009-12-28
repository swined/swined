#ifndef _SILENTLOGGER_H
#define	_SILENTLOGGER_H

#include <iostream>

class SilentLogger : public virtual ILogger {
public:
    SilentLogger() {

    }
    SilentLogger(const SilentLogger& orig) {
        throw Exception("suddenly SilentLogger()");
    }
    virtual ~SilentLogger() {}
    virtual void warn(const std::string& message) {
        std::cerr << "warning: " << message << "\n";
    }
    virtual void debug(const std::string& message) {
        //std::cout << "debug: " << message << "\n";
    }
    virtual void info(const std::string& message) {
        std::cout << "info: " << message << "\n";
    }
    virtual void error(const std::string& message) {
        std::cerr << "error: " << message << "\n";
    }

    private:
};
#endif	/* _SILENTLOGGER_H */

