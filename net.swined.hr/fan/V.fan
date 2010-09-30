
const class V : E, S
{
  
  public const Bool inverse
  public const Str name
  
  public new make(Str name, Bool inverse := false) {
    this.name = name
    this.inverse = inverse
  }
  
  override E andImpl(E e) {
    A(this, [e])
  }

  override E orImpl(E e) {
    A(C.TRUE, [this, e])
  }

  override E notImpl() {
    make(name, !inverse)
  }
  
  override Str toStr() {
    if (inverse)
      return "(!$name)"
    else
      return name
  }
  
  override Bool equals(Obj? o) {
    v := o as V
    if (v == null)
      return false
    else
      return (v.name.equals(name)) && (v.inverse == inverse)
  }

  override E optImpl() {
    this
  }

  override E subImpl(V v) {
    if (equals(v))
      return C.TRUE
    if (equals(v.not))
      return C.FALSE
    return this
  }
  
}
