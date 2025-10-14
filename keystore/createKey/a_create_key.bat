@echo off
setlocal

set /p KEY_NAME=请输入KEY的名称:

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

echo 生成 JKS 文件: %KEYSTORE_NAME%

REM 生成 JKS 
keytool -genkeypair -alias %KEY_ALIAS% -keyalg RSA -keysize 2048 -validity %VALIDITY% -keystore %KEYSTORE_NAME% -storetype PKCS12 -storepass %KEYSTORE_PASS% -keypass %KEY_PASS% -dname "CN=%CN%, OU=%OU%, O=%O%, L=%L%, ST=%ST%, C=%C%"

echo 成功生成 JKS 文件: %KEYSTORE_NAME%

echo.
echo 导出 PKCS12 文件: %KEYSTORE_NAME%.p12

keytool -importkeystore -v -srckeystore %KEYSTORE_NAME% -srcstoretype JKS -srcstorepass %KEYSTORE_PASS% -destkeystore %KEYSTORE_NAME%.p12 -deststoretype PKCS12 -deststorepass %KEYSTORE_PASS% -destkeypass %KEY_PASS% -alias %KEY_ALIAS% -destalias %KEY_ALIAS% -deststoretype pkcs12

echo 成功导出 PKCS12 文件: %KEYSTORE_NAME%.p12

echo.
echo 文件生成完成。

endlocal
pause