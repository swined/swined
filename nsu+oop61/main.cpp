#include <stdlib.h>
#include <iostream>
#include "Shape.h"

using std::cin;
using std::cout;

const Shape& bestShape(const Shape& shape, int depth) {
    if (depth <= 0)
        return shape;
    const Shape& shape2 = Shape::create();
    cout << "comparing shapes:\n";
    shape2.print();
    shape.print();
    if (shape.area() > shape2.area()) {
        return bestShape(shape, depth - 1);
    } else {
        return bestShape(shape2, depth - 1);
    }
}

int main(int argc, char** argv) {
    const Shape& s1 = Shape::create();
    const Shape& s2 = Shape::create();
    s1.print();
    s2.print();
    s1.print();
    return EXIT_SUCCESS;
    int c;
    cout << "enter shape count\n";
    cin >> c;
    const Shape& shape = bestShape(Shape::create(), c - 1);
    cout << "biggest shape:\n";
    shape.print();
    return (EXIT_SUCCESS);
}

