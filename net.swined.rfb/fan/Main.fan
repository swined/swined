using inet

class Main
{
	public static Void main(Str[] args)
	{
    TcpSocket sock := TcpSocket()
    sock.connect(IpAddr("localhost"), 5900, 1sec)
    ver := ServerVersionMsg.read(sock.in)
    echo("ver : $ver")
    sock.out.writeBuf(ClientVersionMsg(3, 8).buf.seek(0))
    security := ServerSecurityTypesMsg.read(sock.in)
    echo("security : ${security.types}")
    echo("read : ${sock.in.readAllStr}")
	}
}
