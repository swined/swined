
const class SetEncodingsMsg : IClientMsg
{
  
  override const Int messageLength
  
  override const Int messageType := 2 // U8
  // padding 1 byte
  // numberOfEncodings U2
  public const Int[] encodings // xU4
  
  public new make(Int[] encodings) {
    messageLength = 4 + 4 * encodings.size
    this.encodings = encodings
  }
  
  override Buf write() {
    buf := head(1).writeI2(encodings.size)
    encodings.each { buf.writeI4(it) }
    return buf
  }
  
}
