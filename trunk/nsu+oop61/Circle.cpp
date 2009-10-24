#include "Circle.h"
#include <iostream>

using std::cin;
using std::cout;

Circle::Circle(double r): m_r(r) {}

Shape* Circle::create() {
    cout << "r = \n";
    double r;
    cin >> r;
    return new Circle(r);
}

void Circle::print() const {
    cout << "circle: r = " << m_r << "\n";
}

double Circle::area() const {
    return m_r * m_r * 3.14;
}