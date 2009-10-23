#include "Square.h"

Square::Square(double a): m_a(a) {}

const Shape& Square::create() {
    cout << "a = \n";
    double a;
    cin >> a;
    const Square& shape(a);
    return shape;
}

void Square::print() const {
    cout << "square: side = " << m_a << "\n";
}

double Square::area() const {
    return m_a * m_a;
}