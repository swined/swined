#ifndef _STRINGUTILS_H
#define	_STRINGUTILS_H

class StringUtils {
public:
    static std::vector<std::string> split(const std::string& s, char b) {
        return split(s, b, 0);
    }
    static std::vector<std::string> split(const std::string& s, char b, int c) {
        std::vector<std::string> r;
        int p = 0;
        int i = 0;
        while ((i = s.find(b, p)) != std::string::npos) {
            if (c != 0 && r.size() > c - 2) {
                r.push_back(s.substr(p, s.length() - p));
                break;
            }
            r.push_back(s.substr(p, i - p));
            p = i + 1;
        }
        return r;
    }
private:

};

#endif	/* _STRINGUTILS_H */

