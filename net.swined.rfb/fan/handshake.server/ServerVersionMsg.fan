
const class ServerVersionMsg 
{
  
  public const Int major
  public const Int minor
  
  new read(InStream in) {
    if (in.readChars(4) != "RFB ") throw Err("unexpected data")
    major = in.readChars(3).toInt
    if (in.readChars(1) != ".") throw Err("unexpected data")
    minor = in.readChars(3).toInt
    if (in.readChars(1) != "\n") throw Err("unexpected data")
  }
  
  override Str toStr() {
    "${major}.${minor}"
  }
  
}
