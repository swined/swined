#include <iostream>
#include "Shape.h"
#include "Square.h"
#include "Rectangle.h"
#include "Circle.h"

using std::cin;
using std::cout;

Shape* Shape::create() {
    int type;
    cout << "enter shape type\n";
    cin >> type;
    switch (type) {
        case 1: return Square::create();
        case 2: return Rectangle::create();
        case 3: return Circle::create();
        default: throw "unknown shape type";
    }
}