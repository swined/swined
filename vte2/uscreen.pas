unit uscreen;

interface

type
 TChar=record
  ch: char;
  atr: byte;
 end;
 TBuffer=array [1..80*25] of TChar;
 PScreen=^TScreen;
 TScreen=object
  Buf,FBuf: TBuffer;
  constructor Create;
  procedure Clr;
  procedure Copy(scr: PScreen; all: boolean);
  procedure PutChar(c: char; x,y,ncl,nbg: byte);
  procedure Update;
 end;

implementation

uses vpsyslow;

//syswrtcharstratt

constructor TScreen.Create;
var c: TChar;
    i: integer;
begin
 c.ch:=' '; c.atr:=7;
 for i:=1 to 80*25 do begin Buf[i]:=c; FBuf[i]:=c; end;
end;

procedure TScreen.Clr;
var c: TChar;
    i: integer;
begin
 c.ch:=' '; c.atr:=7;
 for i:=1 to 80*25 do Buf[i]:=c;
end;

procedure TScreen.Copy(scr: PScreen; all: boolean);
var i: integer;
begin
 for i:=1 to 80*25 do
  if ((not ((buf[i].ch=#$ff) and (buf[i].atr=$ff))) or (all))
   then Buf[i]:=scr^.Buf[i];
end;

procedure TScreen.PutChar(c: char; x,y,ncl,nbg: byte);
begin
 with Buf[x+(y-1)*80] do
  begin ch:=c; atr:=ncl+16*nbg; end;
end;

procedure TScreen.Update;
var i,ca: integer;
begin
 ca:=$ff;
 for i:=1 to 80*25 do
  if (Buf[i].Ch<>FBuf[i].Ch) or (Buf[i].Atr<>FBuf[i].Atr) then
   begin
    FBuf[i]:=Buf[i];
    SysWrtCharStrAtt(@Buf[i].Ch,1,(i-1) mod 80,(i-1) div 80,Buf[i].Atr);
   end;
end;

end.
