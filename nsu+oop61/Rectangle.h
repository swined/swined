#ifndef _RECTANGLE_H
#define	_RECTANGLE_H

#include "Shape.h"

class Rectangle : public Shape {

public:
    static Shape* create();
    virtual void print() const;
    virtual double area() const;

private:
    const double m_a;
    const double m_b;
    Rectangle(double a, double b);

};

#endif	/* _RECTANGLE_H */

