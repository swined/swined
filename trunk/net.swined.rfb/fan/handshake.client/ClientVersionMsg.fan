
const class ClientVersionMsg
{
  
  public const Int major
  public const Int minor
  
  new make(Int major, Int minor) {
    this.major = major
    this.minor = minor
  }
  
  public Buf buf() {
    return Buf {
      endian = Endian.big
    }.writeChars("RFB ${major.toStr.padl(3, '0')}.${minor.toStr.padl(3, '0')}\n")
  }
  
}
