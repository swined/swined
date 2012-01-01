set POCKETBOOKSDK=c:/pbsdk

if not %POCKETBOOKSDK%.==. goto C1
echo Environment variable POCKETBOOKSDK is not set
pause
:C1
set PATH=%POCKETBOOKSDK%\arm-linux\bin;%POCKETBOOKSDK%\bin;%PATH%

set INCLUDE=-I/arm-linux/include
set LIBS=-linkview -lfreetype -ljpeg -lz -lssl -lcrypto -lm -lcurl
set OUTPUT=twitter.app

rm -f %OUTPUT%

gcc -Wall -O2 -fomit-frame-pointer %INCLUDE% -o %OUTPUT% *.c %LIBS% -DHAVE_CURL -DVERSION=LIBOAUTH_VERSION
if errorlevel 1 goto L_ER
strip %OUTPUT%

exit 0

:L_ER
exit 1

