#ifndef _STRINGUTILS_H
#define	_STRINGUTILS_H

class StringUtils {
public:
    static std::string itoa(int v) {
        std::stringstream ss;
        ss << v;
        return ss.str();
    }
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
                p = s.length();
                break;
            }
            r.push_back(s.substr(p, i - p));
            p = i + 1;
        }
        if (p < s.length())
            r.push_back(s.substr(p, s.length() - p));
        if (c != 0 && r.size() != c)
            throw Exception("split(" + s + ", '" + b + "', " + itoa(c) + ") failed: " + itoa(r.size()) + " of " + itoa(c) + " items found");
        return r;
    }
private:

};

#endif	/* _STRINGUTILS_H */

