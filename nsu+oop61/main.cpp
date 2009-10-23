#include <stdlib.h>
#include <iostream>
#include "Shape.h"

using std::cin;
using std::cout;

const Shape& bestShape(const Shape& shape, int depth) {
    if (depth <= 0)
        return shape;
    const Shape& shape2 = Shape::create();
    if (shape.area() > shape2.area()) {
        return shape;
    } else {
        return shape2;
    }
}

int main(int argc, char** argv) {
    int c;
    cout << "enter shape count\n";
    cin >> c;
    const Shape& shape = bestShape(Shape::create(), c - 1);
    cout << "biggest shape:\n";
    shape.print();
    return (EXIT_SUCCESS);
}

