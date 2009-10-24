#include "Rectangle.h"
#include <iostream>

using std::cin;
using std::cout;

Rectangle::Rectangle(double a, double b): m_a(a), m_b(b) {}

Shape* Rectangle::create() {
    double a, b;
    cout << "a = \n";
    cin >> a;
    cout << "b = \n";
    cin >> b;
    return new Rectangle(a, b);
}

void Rectangle::print() const {
    cout << "rectangle: a = " << m_a << ", b = " << m_b << "\n";
}

double Rectangle::area() const {
    return m_a * m_b;
}