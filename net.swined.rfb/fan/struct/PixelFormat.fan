
const class PixelFormat
{
  
  const Int bitsPerPixel // U8
  const Int depth // U8
  const Int bigEndianFlag // U8
  const Int trueColorFlag // U8
  const Int redMax // U16
  const Int greenMax // U16
  const Int blueMax // U16
  const Int redShift // U8
  const Int greenShift // U8
  const Int blueShift // U8
  // padding 3 bytes
  
  public new make(|This->| f) { f(this) }
  
  public Buf buf() {
    Buf {
      endian = Endian.big
    }
    .write(bitsPerPixel)
    .write(depth)
    .write(bigEndianFlag)
    .write(trueColorFlag)
    .writeI2(redMax)
    .writeI2(greenMax)
    .writeI2(blueMax)
    .write(redShift)
    .write(greenShift)
    .write(blueShift)
    .write(0)
    .write(0)
    .write(0)
  }
  
}
