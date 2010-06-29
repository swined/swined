
const class ServerSecurityTypeMsg
{
  
  public const SecurityType type
  
  public new read(InStream in) {
    type = SecurityType.create(in.readU4)
  }
  
}
