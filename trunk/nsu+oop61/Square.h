#ifndef _SQUARE_H
#define	_SQUARE_H

#include "Shape.h"

class Square: public Shape {

public:
    static const Shape& create();
    virtual void print() const;
    virtual double area() const;

private:
    const double m_a;
    Square(double a);

};

#endif	/* _SQUARE_H */

