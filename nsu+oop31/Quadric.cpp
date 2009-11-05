#include "Quadric.h"
#include <cmath>

Quadric::Quadric() : m_a(1), m_b(0), m_c(-1) {
    solve();
}

void Quadric::solve() {
    double d = m_b * m_b - 4 * m_a * m_c;
    if (d == 0) {
        m_roots = 1;
        m_root1 = (-m_b) / (2 * m_a);
        m_root2 = m_root1;
        return;
    }
    if (d > 0) {
        m_roots = 2;
        m_root1 = (-m_b + sqrt(d)) / (2 * m_a);
        m_root2 = (-m_b - sqrt(d)) / (2 * m_a);
        return;
    }
    if (d < 0) {
        m_roots = 0;
        return;
    }
}

void Quadric::setA(double a) {
    m_a = a;
    solve();
}

void Quadric::setB(double b) {
    m_b = b;
    solve();
}

void Quadric::setC(double c) {
    m_c = c;
    solve();
}

int Quadric::getRoots() const {
    return m_roots;
}

double Quadric::getX1() const {
    return m_root1;
}

double Quadric::getX2() const {
    return m_root2;
}