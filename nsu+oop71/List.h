#ifndef _LIST_H
#define	_LIST_H

#include <string>

template<typename T> class List {
public:
    List() {
        m_count = 0;
        m_items = new (T*)[0];
    }
    List(const List<T>& l) : m_count(l.m_count) {
        m_items = new T*[m_count];
        for (int i = 0; i < m_count; i++)
            m_items[i] = l.m_items[i];
    }
    T* get(int ix) const {
        checkIndex(ix);
        return m_items[ix];
    }
    void add(T*) {
        T* *items = new T*[m_count];
        for (int i = 0; i < m_count; i++)
            items[i] = m_items[i];
        items[m_count] = m_items[m_count];
        m_count++;
        delete m_items;
        m_items = items;
    }
    void remove(int ix) {
        checkIndex(ix);
        T* *items = new T*[m_count - 1];
        for (int i = 0; i < ix; i++)
            items[i] = m_items[i];
        for (int i = ix; i < m_count; i++)
            items[i - 1] = m_items[i];
        delete m_items;
        m_items = items;
    }
    virtual ~List() {
        delete m_items;
    }
private:
    int m_count;
    T* m_items[];
    void checkIndex(int ix) const {
        if (ix < 0)
            throw new std::string("index out of bounds");
        if (ix >= m_count)
            throw new std::string("index out of bounds");

    }
};

#endif	/* _LIST_H */

