#include <stdlib.h>
#include "Quadric.h"
#include <iostream>

using std::cout;

int main(int argc, char** argv) {
    Quadric q;
    cout << q.getX1() << "\n"; // выведет 1
    cout << q.getX2() << "\n"; // выведет -1
    q.setA(1);
    q.setB(2);
    q.setC(1);
    cout << q.getRoots() << "\n"; // выведет 1
    cout << q.getX1() << "\n"; // выведет 1
    return (EXIT_SUCCESS);
}

