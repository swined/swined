
const class ColourMapEntry
{
  
  public const Int red // U16
  public const Int green // U16
  public const Int blue // U16
  
  new make(InStream in) {
    red = in.readU2
    green = in.readU2
    blue = in.readU2
  }
  
}
