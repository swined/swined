
const class C : E
{
 
  public static const C TRUE := make(true)
  public static const C FALSE := make(false)
  public const Bool value
  
  private new make(Bool value) {
    this.value = value
  }
  
  override E and(E e) {
    if (value)
      return e
    else
      return FALSE
  }

  override E or(E e) {
    if (value)
      return TRUE
    else
      return e
  }

  override E not() {
    if (value)
      return FALSE
    else
      return TRUE
  }
  
}
