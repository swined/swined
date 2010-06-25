
const mixin IServerMsg
{

  abstract Int messageType() 
  
  protected Void checkMessageType(InStream in, Int skip := 0) {
    mt := in.peek
    if (mt != messageType) throw Err("unexpected message type: $mt != $messageType")
    (skip + 1).times { in.readU1 }
  }
  
}
