
const class A : E
{
  
  public const S v
  public const E[] e
  
  public new make(S v, E[] e) {
    this.v = v
    this.e = e
  }

  override E andImpl(E e) {
    A(v, this.e.map |E x->E| { x.and(e) })
  }

  override E orImpl(E e) {
    A(C.TRUE, [this, e])
  }

  override E notImpl() {
    x := C.TRUE
    e.each { x = x.and(it.not) }
    return A(v.not, e)
  }
 
  override Str toStr() {
    "$v->$e"
  }

  override E optImpl() {
    if (v == C.FALSE)
      return C.FALSE
    if (e.contains(C.TRUE))
      return v
    if (e.contains(C.FALSE) && e.size == 1)
      return C.FALSE
    if (v is V)
      return A(v, e.map |E x->E| { x.subImpl(v).optImpl })
    return this
  }
  
  override E subImpl(V v) {
    A(this.v.subImpl(v), e.map |E x->E| { x.subImpl(v) })
  }
  
}
