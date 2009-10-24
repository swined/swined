#ifndef _CIRCLE_H
#define	_CIRCLE_H

#include "Shape.h"

class Circle : public Shape {

public:
    static Shape* create();
    virtual void print() const;
    virtual double area() const;

private:
    const double m_r;
    Circle(double r);

};

#endif	/* _CIRCLE_H */

