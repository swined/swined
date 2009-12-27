#ifndef _KEYGENERATOR_H
#define	_KEYGENERATOR_H

class KeyGenerator {
public:
    static std::string generateKey(std::string lock) {
        int len = lock.length();
        std::string key = lock;
        for(int i = 1; i < len; i++)
                key[i] = lock[i] ^ lock[i-1];
        key[0] = lock[0] ^ lock[len-1] ^ lock[len-2] ^ 5;
        for(int i = 0; i < len; i++)
                key[i] = ((key[i]<<4) & 0xF0) | ((key[i]>>4) & 0x0F);
        dcnReplace(key, (char)  0, "/%DCN000%/");
        dcnReplace(key, (char)  5, "/%DCN005%/");
        dcnReplace(key, (char) 36, "/%DCN036%/");
        dcnReplace(key, (char) 96, "/%DCN096%/");
        dcnReplace(key, (char)124, "/%DCN124%/");
        dcnReplace(key, (char)126, "/%DCN126%/");
        return key;
}
private:

    static void dcnReplace(std::string& s, char b, const std::string& r) {
        int ix = 0;
        while ((ix = s.find(b)) != -1)
            s.replace(ix, 1, r);
    }

};

#endif	/* _KEYGENERATOR_H */

