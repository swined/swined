
enum class SecurityType
{

  Invalid(0),
  None(1),
  VNCAuth(2)
  
  public const Int type
  
  private new make(Int type) {
    this.type = type
  }
  
  public static SecurityType? create(Int type) {
    return SecurityType.vals.find |SecurityType t -> Bool| {
      t.type == type 
    }
  }
  
}
