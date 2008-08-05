unit ubuttons;

interface

uses uscreen;

type
 TButton=object
  public
   BGColor, TxtColor, ActiveBGColor, ActiveTextColor: integer;
   Active: boolean;
   Text: string;
   CScreen: PScreen;
   OScreen: TScreen;
   constructor Create(L,T: integer; Txt: string; scr: pscreen);
   procedure SetActive(a: boolean);
   procedure Redraw;
  private
   Top, Left, Width: integer;
 end;

implementation

uses crt;

procedure TButton.SetActive(a: boolean);
begin
 active:=a; redraw; CScreen^.Update;
end;

procedure TButton.Redraw;
var i: integer;
begin
 for i:=1 to length(text) do
  if active then
   CScreen^.PutChar(text[i],left+i-1,top,ActiveTextColor,ActiveBGColor)
  else
   CScreen^.PutChar(text[i],left+i-1,top,TxtColor,BGColor);
 CScreen^.Update;
end;

constructor TButton.Create(L,T: integer; Txt: string; scr: pscreen);
begin
 Top:=T; Left:=L; Width:=length(txt)+2; CScreen:=scr;
 BGColor:=10; TxtColor:=7; ActiveBGColor:=10; ActiveTextColor:=15;
 Text:=' '+Txt+' '; Active:=false;
end;

end.
