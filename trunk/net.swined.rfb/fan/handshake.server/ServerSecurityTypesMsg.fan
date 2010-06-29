
const class ServerSecurityTypesMsg
{
  
  public const SecurityType[] types
  
  public new read(InStream in) {
    c := in.readU1
    types := [,]
    c.times {
      types.add(SecurityType.create(in.readU1))
    }
    this.types = types
  }
  
}
