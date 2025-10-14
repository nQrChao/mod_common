@echo off
setlocal

set /p KEY_NAME=������KEY������:

set KEYSTORE_NAME=%KEY_NAME%.jks
set KEY_ALIAS=%KEY_NAME%
set KEYSTORE_PASS=%KEY_NAME%123
set KEY_PASS=%KEY_NAME%123
set CN=%KEY_NAME%
set OU=%KEY_NAME%
set O=%KEY_NAME%
set L=%KEY_NAME%
set ST=%KEY_NAME%
set C=%KEY_NAME%
set VALIDITY=9999

echo ���� JKS �ļ�: %KEYSTORE_NAME%

REM ���� JKS 
keytool -genkeypair -alias %KEY_ALIAS% -keyalg RSA -keysize 2048 -validity %VALIDITY% -keystore %KEYSTORE_NAME% -storetype PKCS12 -storepass %KEYSTORE_PASS% -keypass %KEY_PASS% -dname "CN=%CN%, OU=%OU%, O=%O%, L=%L%, ST=%ST%, C=%C%"

echo �ɹ����� JKS �ļ�: %KEYSTORE_NAME%

echo.
echo ���� PKCS12 �ļ�: %KEYSTORE_NAME%.p12

keytool -importkeystore -v -srckeystore %KEYSTORE_NAME% -srcstoretype JKS -srcstorepass %KEYSTORE_PASS% -destkeystore %KEYSTORE_NAME%.p12 -deststoretype PKCS12 -deststorepass %KEYSTORE_PASS% -destkeypass %KEY_PASS% -alias %KEY_ALIAS% -destalias %KEY_ALIAS% -deststoretype pkcs12

echo �ɹ����� PKCS12 �ļ�: %KEYSTORE_NAME%.p12

echo.
echo �ļ�������ɡ�

endlocal
pause