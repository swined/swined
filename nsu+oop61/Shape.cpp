#include <iostream>
#include "Shape.h"
#include "Square.h"

using std::cin;
using std::cout;

const Shape& Shape::create() {
    int type;
    cout << "enter shape type\n";
    cin >> type;
    switch (type) {
        case 1: return Square::create();
        default: throw "unknown shape type";
    }
}