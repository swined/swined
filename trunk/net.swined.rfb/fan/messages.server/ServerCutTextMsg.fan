
const class ServerCutTextMsg : IServerMsg
{
  
  override const Int messageType := 3
  public const Str text
  
  new read(InStream in) {
    checkMessageType(in, 3)
    len := in.readU4
    sb := StrBuf()
    len.times { sb.addChar(in.readU1) }
    text = sb.toStr
  }
  
}
