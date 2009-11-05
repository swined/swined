#ifndef _QUADRIC_H
#define	_QUADRIC_H

class Quadric {
public:
    Quadric();
    void setA(double a);
    void setB(double b);
    void setC(double c);
    int getRoots() const;
    double getX1() const;
    double getX2() const;
private:
    double m_a;
    double m_b;
    double m_c;
    double m_roots;
    double m_root1;
    double m_root2;
    void solve();
};

#endif	/* _QUADRIC_H */

