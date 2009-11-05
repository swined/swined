#include <stdlib.h>
#include <iostream>
#include "Complex.h"

Complex operator+(int re, const Complex& c1) {
    Complex sum(re + c1.real, c1.image);
    return sum;
}

std::ostream & operator<<(std::ostream& out, const Complex& c) {
    return out << "(" << c.real << ", " << c.image << ")\n";
}

int main(int argc, char** argv) {
    Complex c(1, 2);
    std::cout << c;
    Complex d(3, 4);
    std::cout << d;
    c += 5;
    std::cout << c;
    c += d;
    std::cout << c;
    c = c + 1;
    std::cout << c;
    c = c + d;
    std::cout << c;
    c -= 5;
    std::cout << c;
    c -= d;
    std::cout << c;
    c = c - 1;
    std::cout << c;
    c = c - d;
    std::cout << c;
    return (EXIT_SUCCESS);
}

