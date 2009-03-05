unit udialog;

interface

uses uform,uscreen,uapp;

type
 TInfoMessage=object(tform)
  public
   constructor create(cpt, Txt: string; napp: papp);
   function run(cscreen: pscreen): integer; virtual;
  private
   Text: string;
 end;
{ TErrorMessage=object(TInfoMessage)
  constructor create(cpt, txt: string);
 end;}
{ TYesNoDialog=object(TForm)
   constructor create(cpt, txt: string);
   function run(cscreen: pscreen): integer; virtual;
  private
   text: string;
 end;}
 procedure ShowMessage(cpt,txt: string; scr: pscreen);
{ procedure ShowError(cpt,txt: string; scr: pscreen);
 function ShowYND(cpt,txt: string; scr: pscreen): integer;}

implementation

uses crt{, ubuttons};

function max(a,b: integer): integer;
begin if a>=b then max:=a else max:=b; end;

{constructor TErrorMessage.Create(cpt, txt: string);
begin
 inherited create(cpt,txt);
 BGColor:=4; TxtColor:=14;
end;}

constructor TInfoMessage.Create(cpt,txt: string; napp: papp);
var w: integer;
begin
 inherited create(napp);
 initialized:=true; active:=true;
 w:=length(txt); if w<max(length(cpt),4) then w:=max(length(cpt),4);
 text:=txt; caption:=cpt;
 BGColor:=7; TxtColor:=0; AFrameColor:=15;
 Top:=10; Left:=38-w div 2; Width:=w+4; height:=7;
 _scr.create;
 napp.psm(ms_deactivate);
end;



{function TInfoMessage.Run(cscreen: pscreen): integer;
var ch: char;
    b: tbutton;
    i: integer;
begin
 b.create(left+width div 2-2,top+4,'OK',CScreen);
 b.active:=true; b.redraw;
 for i:=1 to length(text) do
  CScreen^.PutChar(text[i],left+1+i,top+2,TxtColor,BGColor);
 CScreen^.Update;
 repeat ch:=readkey until (ch=#13) or (ch=#27);
 while keypressed do readkey;
end;}

constructor TYesNoDialog.create(cpt, txt: string);
var w: integer;
begin
 w:=length(txt);
 if w<max(length(cpt),9) then w:=max(length(cpt),9);
 text:=txt; caption:=cpt;
 inherited create(38-w div 2,10,w+4,7,cpt);
 BGColor:=7; TxtColor:=0;
end;

function TYesNoDialog.Run(cscreen: pscreen): integer;
var ch: char;
    by,bn: tbutton;
    i: integer;
begin
 by.create(width div 2-5+left,top+4,'Yes',cscreen); by.active:=true; by.redraw;
 bn.create(width div 2+1+left,top+4,'No',cscreen); bn.active:=false; bn.redraw;
 for i:=1 to length(text) do
  CScreen^.PutChar(text[i],left+1+i,top+2,TxtColor,BGColor);
 CScreen^.Update;
 repeat
  ch:=readkey;
  case ch of
   'y': begin by.setactive(true); bn.setactive(false); end;
   'n': begin by.setactive(false); bn.setactive(true); end;
  end;
 until ch=#13;
 if by.active then run:=1 else run:=0;
end;

procedure ShowMessage(cpt,txt: string; scr: PScreen);
var im: TInfoMessage;
begin im.create(cpt,txt); im.show(true,scr); end;

procedure ShowError(cpt,txt: string; scr: pscreen);
var im: TErrorMessage;
begin im.create(cpt,txt); im.show(true,scr); end;

function ShowYND(cpt,txt: string; scr: pscreen): integer;
var im: TYesNoDialog;
begin im.create(cpt,txt); ShowYND:=im.show(true,scr); end;

end.
