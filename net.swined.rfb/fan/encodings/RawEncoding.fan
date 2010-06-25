
const class RawEncoding : IEncoding
{
  
  public const PixelFormat format
  
  public new make(PixelFormat format) {
    this.format = format
  }

  override IRect read(InStream in, Int w, Int h) {
    RawEncodedRect.read(in, w, h, format.bitsPerPixel / 8)
  }
  
}
