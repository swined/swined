#ifndef _CONSOLELOGGER_H
#define	_CONSOLELOGGER_H

#include <iostream>

class ConsoleLogger : public virtual ILogger {
public:
    ConsoleLogger() {

    }
    ConsoleLogger(const ConsoleLogger& orig) {
        
    }
    virtual ~ConsoleLogger() {}
    virtual void warn(const std::string& message) {
        std::cerr << "warning: " << message << "\n";
    }
    virtual void debug(const std::string& message) {
        std::cout << "debug: " << message << "\n";
    }
    virtual void info(const std::string& message) {
        std::cout << "info: " << message << "\n";
    }
    virtual void error(const std::string& message) {
        std::cerr << "error: " << message << "\n";
    }

private:

};

#endif	/* _CONSOLELOGGER_H */

