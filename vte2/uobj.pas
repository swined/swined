unit uobj;

interface

uses uscreen;

const
 MaxMsgData=2;

const
 ms_exit=1;
 ms_openfile=2;
 ms_noop=3;
 ms_keypressed=4;
 ms_deactivate=5;
 ms_updatewin=6;
 ms_redrawactive=7;
 ms_redrawinactive=8;
 ms_clrscr=9;
 ms_updscr=10;
 ms_nextwin=11;

type
 TMsg=packed record
  Ms: longint;
  Dt: array [0..MaxMsgData-1] of byte;
 end;
 PObj=^TObj;
 TObj=object
  Initialized: boolean;
  app: pointer;
  scr: pscreen;
  constructor Create(a: pointer; s: pscreen);
  procedure MsgProc(msg: TMsg); virtual;
 end;

implementation

constructor TObj.Create(a: pointer; s: pscreen);
begin
 app:=a; scr:=s;
 initialized:=false;
end;

procedure TObj.MsgProc(msg: TMsg);
begin end;

end.
