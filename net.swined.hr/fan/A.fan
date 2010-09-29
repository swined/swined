
const class A : E
{
  
  public const V v
  public const E[] e
  
  public new make(V v, E[] e) {
    this.v = v
    this.e = e
  }

  override E and(E e) {
    A(v, this.e.map |E x| { x.and(e) })
  }

  override E or(E e) {
    A(v, [e])
  }

  override E not() {
    x := C.TRUE
    e.each { x = x.and(it.not) }
    return A(v.not, e)
  }
  
}
