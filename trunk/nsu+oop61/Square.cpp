#include "Square.h"
#include <iostream>

using std::cin;
using std::cout;

Square::Square(double a): m_a(a) {}

Shape* Square::create() {
    cout << "a = \n";
    double a;
    cin >> a;
    return new Square(a);
}

void Square::print() const {
    cout << "square: a = " << m_a << "\n";
}

double Square::area() const {
    return m_a * m_a;
}