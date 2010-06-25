
const class FramebufferUpdateMsg : IServerMsg
{
  
  override const Int messageType := 0
  public const IRect[] data
  
  public new read(InStream in, PixelFormat format) {
    checkMessageType(in, 1)
    data := [,]
    in.readU2.times { data.add(Rectangle.read(in, format)) }
    this.data = data
  }
  
}
