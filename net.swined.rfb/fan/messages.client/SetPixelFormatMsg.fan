
const class SetPixelFormatMsg : IClientMsg
{
  
  override const Int messageLength := 20
  
  override const Int messageType := 0 // U8
  // padding 3 bytes
  const PixelFormat pixelFormat // 16 bytes
  
  public new make(PixelFormat pixelFormat) {
    this.pixelFormat = pixelFormat
  }
  
  override Buf write() {
    head(3)
    .writeBuf(pixelFormat.buf.seek(0))
  }
  
}
