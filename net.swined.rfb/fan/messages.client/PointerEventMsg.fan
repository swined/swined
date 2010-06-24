
const class PointerEventMsg : IClientMsg
{
  
  override const Int messageLength := 6
  
  override const Int messageType := 5 // U8
  public const Int buttonMask // U8
  public const Int x // U16
  public const Int y // U16
  
  public new make(Int buttonMask, Int x, Int y) {
    this.buttonMask = buttonMask
    this.x = x
    this.y = y
  }
  
  override Buf write() {
    head
    .write(buttonMask)
    .writeI2(x)
    .writeI2(y)
  }
  
}
