
class Main
{
	public static Void main(Str[] args)
	{
    SetPixelFormatMsg(PixelFormat { }).buf    
    SetEncodingsMsg([42, 37]).buf
    FramebufferUpdateRequestMsg {} .buf
    KeyEventMsg(true, 42).buf
    PointerEventMsg(1, 2, 3).buf
    ClientCutTextMsg("wtf").buf
	}
}
