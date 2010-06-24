
const class ClientCutTextMsg : IClientMsg 
{
  
  override const Int messageLength 
  
  override const Int messageType := 6 // U8
  // padding 3 bytes
  // length U32
  public const Str text // xU8
  
  public new make(Str text) {
    messageLength = 8 + text.size
    this.text = text 
  }
  
  override Buf write() {
    buf := head(3).writeI4(text.size)
    text.each { buf.write(it) }
    return buf
  }
  
}
