#ifndef _ILOGGER_H
#define	_ILOGGER_H

class ILogger {
public:
    virtual void info(const std::string& m) = 0;
    virtual void debug(const std::string& m) = 0;
    virtual void warn(const std::string& m) = 0;
    virtual void error(const std::string& m) = 0;
    virtual ~ILogger() {};
private:

};

#endif	/* _ILOGGER_H */

