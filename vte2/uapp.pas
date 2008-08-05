unit uapp;

interface

uses uscreen,uobj;

const
 MaxWndCnt=100;
 MaxMsgCnt=100;

type
 PApp=^TApp;
 TApp=object(TObj)
  _mmc,_mc: longint;
  law: longint;
  Wnd: array [0..MaxWndCnt-1] of TObj;
  MsgS,MsgC: longint;
  Msg: array [0..MaxMsgCnt-1] of TMsg;
  procedure MsgProc(nmsg: tmsg); virtual;
  procedure FatalErr(txt: string);
  procedure PeekMsg;
  procedure PSM(ms: longint);
  procedure PostMsg(nmsg: TMsg);
  procedure Run;
 end;

implementation

uses crt,sysutils;

procedure log(txt: ansistring);
var f: text;
begin
 assign(f,'s:\vte2\vte.log');
 append(f);
 writeln(f,txt);
 close(f);
end;

procedure nlog;
var f: text;
begin
 assign(f,'s:\vte2\vte.log');
 rewrite(f);
 close(f);
end;

procedure logmsg(msg: tmsg);
var s: ansistring;
    i: integer;
begin
 s:='MSG: ms='+inttostr(msg.ms)+' dt=[';
 for i:=0 to MaxMsgData-1 do s:=s+inttostr(msg.dt[i])+',';
 s[length(s)]:=']';
 log(s);
end;

procedure TApp.MsgProc(nmsg: tmsg);
var i: integer;
begin
 if nmsg.ms=ms_keypressed then
  if (nmsg.dt[0]=27) then
   psm(ms_exit);
 if nmsg.ms=ms_clrscr then papp(app)^.scr.clr;
 if nmsg.ms=ms_updscr then papp(app)^.scr.update;
 if nmsg.ms=ms_nextwin then
  begin

  end;
 logmsg(nmsg);
end;

procedure TApp.FatalErr(txt: string);
begin
 clrscr;
 gotoxy(1,1);
 textcolor(white);
 textbackground(black);
 writeln('FATAL ERROR: ',txt,'!');
 halt;
end;

procedure TApp.PSM(ms: longint);
var m: tmsg;
begin
 m.ms:=ms;
 postmsg(m);
end;

procedure TApp.PostMsg(nmsg: TMsg);
var mex: boolean;
begin
 if MsgC=MaxMsgCnt then FatalErr('Message queue overflow');
 if (msgc>0) and (msg[(msgs+msgc-1) mod MaxMsgCnt].ms=ms_exit) then
  begin dec(msgc); mex:=true; end else mex:=false;
 if ((nmsg.ms<>ms_exit) and mex) or not mex then
  begin
   inc(msgc); msg[(msgs+msgc-1) mod MaxMsgCnt]:=nmsg;
   if msgc>_mmc then _mmc:=msgc;
  end;
 if mex then psm(ms_exit);
end;

procedure TApp.PeekMsg;
var i: integer;
begin
 if msgc=0 then exit;
 inc(_mc);
 msgproc(msg[msgs mod MaxMsgCnt]);
 for i:=0 to MaxWndCnt-1 do
  if wnd[i].initialized then
   wnd[i].msgproc(msg[msgs mod MaxMsgCnt]);
 inc(msgs); dec(msgc);
 msgs:=msgs mod MaxMsgCnt;
end;

procedure TApp.Run;
var i: integer;
    m: tmsg;
    ch,che: char;
begin
 nlog;
 scr^.create; _mmc:=0; _mc:=0; MsgS:=0; MsgC:=0; law:=0;
 for i:=0 to MaxWndCnt-1 do wnd[i].create(scr);
// psm(ms_openfile);
 repeat
  peekmsg;
  if keypressed then
   begin
    ch:=readkey; che:=#0; if ch=#0 then che:=readkey;
    m.ms:=ms_keypressed; m.dt[0]:=ord(ch); m.dt[1]:=ord(che);
    postmsg(m);
   end;
 until (msgc=1) and (msg[msgs mod MaxMsgCnt].ms=ms_exit);
 log('STAT: mmc='+inttostr(_mmc));
end;

end.
