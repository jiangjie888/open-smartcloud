@echo off
rem dotnet 项目部署
echo ============================begin deploy=======================================
rem set CUR_TIME=%date:~0,4%-%date:~5,2%-%date:~8,2% %time:~0,2%:%time:~3,2%:%time:~6,2%
set CUR_TIME=%date:~0,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%%time:~6,2%
set SSH_DIR=C:\Users\Administrator\ssh_dir\dotnet_publish
set DEPLOY_DIR=C:\webapps
set "PROJECTGROUP_DIR=%1"
set "PROJECT_DIR=%2"
set "PROJECT_ENV=%3"
set "VIRTUAL_DIR=%4"
set IIS_SITE=%PROJECTGROUP_DIR%_%PROJECT_DIR%_%PROJECT_ENV%
if NOT %PROJECT_ENV%==pod (
set PROJECT_DIR=%PROJECT_DIR%_%PROJECT_ENV%
)
echo %PROJECT_DIR%
if "%PROJECTGROUP_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
if "%PROJECT_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
if "%PROJECT_ENV%"=="" (
echo This environment prams is needed to run this program
exit 1
)
if "%VIRTUAL_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
C:
cd %SSH_DIR%\%PROJECTGROUP_DIR%
if exist %SSH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%.rar (
rem 停止IIS站点
@C:\Windows\System32\inetsrv\appcmd.exe stop site "%IIS_SITE%"

rem 先备份上一次内容
echo ============================begin back site dir=======================================
WinRAR.exe a -k -r -s -m3 -o+ -ep1 -y -inul -xApp_Data\Logs -xLogs.txt %DEPLOY_DIR%\%PROJECTGROUP_DIR%\bak\%PROJECT_DIR%_%CUR_TIME%.rar %DEPLOY_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\
echo ============================end back site dir=======================================
rem 备份完后删除

rem 解压到制定目录下
echo ============================begin unrar to site dir====================================
WinRAR.exe x -y -o+ %SSH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%.rar %DEPLOY_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\%VIRTUAL_DIR%\

rem 拷贝appConfig.js 
copy %DEPLOY_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\%VIRTUAL_DIR%\appConfig.js %DEPLOY_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\%VIRTUAL_DIR%\static /y

echo ============================end unrar to site dir=======================================
rem 启动IIS站点
@C:\Windows\System32\inetsrv\appcmd.exe start site "%IIS_SITE%"
echo deploy ok
del /Q %SSH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%.rar
) else (
echo deploy excaption:not found the deploy package
exit 1
)
echo ============================end deploy=======================================