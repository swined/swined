#ifndef _SHAPE_H
#define	_SHAPE_H

class Shape {
public:
    static const Shape& create();
    virtual void print() const = 0;
    virtual double area() const = 0;
private:

};

#endif	/* _SHAPE_H */

