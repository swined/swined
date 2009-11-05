#ifndef _COMPLEX_H
#define	_COMPLEX_H

#include <iostream>

class Complex {
private:
    double real, image;
public:
    Complex(double re, double im);
    double getReal() const;
    double getImage() const;
    Complex& operator+=(const Complex& obj);
    Complex& operator+=(const double re);
    Complex operator+(const Complex& c) const;
    Complex operator+(const double re) const;
    Complex& operator-=(const Complex& obj);
    Complex& operator-=(const double re);
    Complex operator-(const Complex& c) const;
    Complex operator-(const double re) const;
    Complex operator*(const Complex& c) const;
    Complex operator*(const double re) const;
    double operator[] (const int pos) const;
    friend std::ostream& operator<<(std::ostream& out, const Complex& c);
    friend Complex operator+(const int d, const Complex& c);
};

#endif	/* _COMPLEX_H */

