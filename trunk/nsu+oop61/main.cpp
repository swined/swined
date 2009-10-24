#include <stdlib.h>
#include <iostream>
#include "Shape.h"

using std::cin;
using std::cout;

int main(int argc, char** argv) {
    int c;
    cout << "enter shape count\n";
    cin >> c;
    Shape* shape = Shape::create();
    while (--c > 0) {
        Shape* shape2 = Shape::create();
        if (shape->area() > shape2->area()) {
            delete shape2;
        } else {
            delete shape;
            shape = shape2;
        }
    }
    cout << "biggest shape:\n";
    shape->print();
    delete shape;
    return (EXIT_SUCCESS);
}

