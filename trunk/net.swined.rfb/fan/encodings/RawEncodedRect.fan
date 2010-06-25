
const class RawEncodedRect : IRect
{

  public const RawPixel[] pixels
  
  public new read(InStream in, Int w, Int h, Int bytesPerPixel) {
    pixels := [,]
    (w*h).times { pixels.add(RawPixel.read(in, bytesPerPixel)) }
    this.pixels = pixels
  }
  
}

const class RawPixel {
  
  public const Int[] data
  
  public new read(InStream in, Int bytesPerPixel) {
    data := [,]
    bytesPerPixel.times { data.add(in.readU1) }
    this.data = data
  }
  
}