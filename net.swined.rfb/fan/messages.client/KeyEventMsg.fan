
const class KeyEventMsg : IClientMsg
{
  
  override const Int messageLength := 8
  
  override const Int messageType := 4 // U8
  public const Bool down // U8
  // padding 2 bytes
  public const Int key // U32
  
  public new make(Bool down, Int key) {
    this.down = down
    this.key = key
  }
  
  override Buf write() {
    pad(head
    .writeBool(down), 2)
    .writeI4(key)
  }
  
}
