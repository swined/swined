
const class Rectangle
{
  
  public const Int x // U16
  public const Int y // U16
  public const Int w // U16
  public const Int h // U16
  public const Int encType // S32
  public const IRect data
  
  new read(InStream in, PixelFormat format) {
    x = in.readU2
    y = in.readU2
    w = in.readU2
    h = in.readU2
    encType = in.readS4
    data = IEncoding.get(encType, format).read(in, w, h)
  }
  
}
