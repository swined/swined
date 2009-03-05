unit uform;

interface

uses uscreen,uobj,uapp;

type
 TForm=object(tobj)
  BgColor, TxtColor, AFrameColor, IFrameColor: integer;
  Top, Left, Width, Height: integer;
  _scr: tscreen;
  App: PApp;
  Active: boolean;
  Caption: string;
  procedure MsgProc(nmsg: tmsg); virtual;
  procedure Draw;
 end;

implementation

uses crt;

procedure TForm.Draw;
var i,j,FrameColor: integer;
    cpt: string;
begin
 if active then FrameColor:=AFrameColor else FrameColor:=IFrameColor;
 _scr.PutChar('É',left,top,FrameColor,BGColor);
 _scr.PutChar('È',left,top+height-1,FrameColor,BGColor);
 _scr.PutChar('»',left+width-1,top,FrameColor,BGColor);
 _scr.PutChar('¼',left+width-1,top+height-1,FrameColor,BGColor);
 for i:=left+1 to left+width-2 do
 _scr.PutChar('Í',i,top,FrameColor,BGColor);
 for i:=left+1 to left+width-2 do
 _scr.PutChar('Í',i,top+height-1,FrameColor,BGColor);
 for i:=top+1 to top+height-2 do
 _scr.PutChar('º',left,i,FrameColor,BGColor);
 for i:=top+1 to top+height-2 do
 _scr.PutChar('º',left+width-1,i,FrameColor,BGColor);
 cpt:=caption;
 while (length(cpt)>0) and (length(cpt)>width-4) do delete(cpt,length(cpt),1);
 for i:=1 to length(cpt) do
 _scr.PutChar(cpt[i],left+(width-length(cpt)) div 2+i-1,top,FrameColor,BGColor);
end;

procedure TForm.MsgProc(nmsg: tmsg);
begin
 case nmsg.ms of
  ms_deactivate: active:=false;
  ms_updatewin: draw;
  ms_redrawactive: if active then scr^.copy(@_scr,false);
  ms_redrawinactive: if not active then scr^.copy(@_scr,false);
 end;
end;

end.
