#include "Complex.h"

#define debug std::cout << __func__ << "()\n";

Complex::Complex(double re, double im) : real(re), image(im) {
    debug;
}

double Complex::getReal() const {
    debug;
    return real;
}

double Complex::getImage() const {
    debug;
    return image;
}

Complex& Complex::operator+=(const Complex& obj) {
    debug;
    real += obj.real;
    image = obj.image;
    return *this;
}

Complex& Complex::operator+=(const double re) {
    debug;
    real += re;
    return *this;
}

Complex Complex::operator+(const Complex& c) const {
    debug;
    Complex sum(real + c[0], image + c[1]);
    return sum;
}

Complex Complex::operator+(const double re) const {
    Complex sum(real + re, image);
    return sum;
}

Complex& Complex::operator-=(const Complex& obj) {
    debug;
    real -= obj.real;
    image = obj.image;
    return *this;
}

Complex& Complex::operator-=(const double re) {
    debug;
    real -= re;
    return *this;
}

Complex Complex::operator-(const Complex& c) const {
    debug;
    Complex sum(real - c[0], image - c[1]);
    return sum;
}

Complex Complex::operator-(const double re) const {
    Complex sum(real - re, image);
    return sum;
}

Complex Complex::operator*(const Complex& c) const {
    debug;
    Complex sum(real * c[0] + image * c[1], real * c[1] - image * c[0]);
    return sum;
}

Complex Complex::operator*(const double re) const {
    Complex sum(real * re, image * re);
    return sum;
}

double Complex::operator[] (const int pos) const {
    if (pos == 0)
        return real;
    else
        return image;
}
