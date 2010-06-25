
const class BellMsg : IServerMsg
{
  
  override const Int messageType := 2 
  
  public new read(InStream in) {
    checkMessageType(in)
  }
  
}
