
const mixin IClientMsg
{

  public abstract Int messageLength() 
  
  public abstract Int messageType()
  
  public Buf buf() {
    check(write)
  }

  protected abstract Buf write()
  
  protected Buf head(Int padding := 0) {
    pad(Buf {
      endian = Endian.big
    }
    .write(messageType), padding)
  }
 
  protected Buf check(Buf buf) {
    if (buf.size != messageLength)
      throw Err("message length does not match: ${buf.size} != ${messageLength}")
    return buf
  }
  
  protected Buf pad(Buf b, Int l) {
    l.times {
      b.write(0)
    }
    return b
  }
  
}