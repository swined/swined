
const class V : E, S
{
  
  public const Bool inverse
  public const Str name
  
  public new make(Str name, Bool inverse) {
    this.name = name
    this.inverse = inverse
  }
  
  override E and(E e) {
    A(this, [e])
  }

  override E or(E e) {
    A(C.TRUE, [this, e])
  }

  override E not() {
    make(name, !inverse)
  }
  
  override Str toStr() {
    if (inverse)
      return "(!$name)"
    else
      return name
  }
  
}
