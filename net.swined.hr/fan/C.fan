
const class C : E, S
{
 
  public static const C TRUE := make(true)
  public static const C FALSE := make(false)
  public const Bool value
  
  private new make(Bool value) {
    this.value = value
  }
  
  override E andImpl(E e) {
    if (value)
      return e
    else
      return FALSE
  }

  override E orImpl(E e) {
    if (value)
      return TRUE
    else
      return e
  }

  override E notImpl() {
    if (value)
      return FALSE
    else
      return TRUE
  }
  
  override Str toStr() {
    value ? "1" : "0"
  }
  
  override E optImpl() {
    this
  }
  
  override E subImpl(V v) {
    this
  }

  override Bool equals(Obj? o) {
    c := o as C
    if (c == null)
      return false
    else
      return c.value == value
  }
  
}
