
const mixin IEncoding
{

  abstract IRect read(InStream in, Int w, Int h)
  
  public static IEncoding get(Int type, PixelFormat format) {
    switch (type) {
      case 0: return RawEncoding(format)
      default: throw Err("unknown encoding id: $type")
    }
  }
  
}
