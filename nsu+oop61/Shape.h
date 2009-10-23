/* 
 * File:   Shape.h
 * Author: sw
 *
 * Created on October 23, 2009, 5:59 PM
 */

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

