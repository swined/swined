
const class FramebufferUpdateRequestMsg : IClientMsg
{
  
  override const Int messageLength := 10
  
  override const Int messageType := 3 // U8
  public const Bool incremental // U8
  public const Int x // U16
  public const Int y // U16
  public const Int w // U16
  public const Int h // U16

  public new make(|This->| f) { f(this) }
  
  override Buf write() {
    head
    .writeBool(incremental)
    .writeI2(x)
    .writeI2(y)
    .writeI2(w)
    .writeI2(h)
  }
  
}
