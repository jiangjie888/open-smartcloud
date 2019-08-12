@echo off
rem dotnet 项目发布
echo ============================begin build=======================================
set PUBLISH_DIR=C:\JenkinsPublishSpace\gitCode
set "PROJECTGROUP_DIR=%1"
set "PROJECT_DIR=%2"
if "%PROJECTGROUP_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
if "%PROJECT_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
if "%PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\src\WebApp.Web.Host"=="" (
echo fetch code failed
exit 1
)
cd %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\src\WebApp.Web.Host
rem dotnet build
if exist %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\release (
	rd /S /Q %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\release
)
md %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\release
rem dotnet msbuild /t:Publish /p:OutputPath=D:\jekinsWorkspace\dotnet_publish\workflow /p:Configuration=Release
dotnet msbuild
dotnet publish -o %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\release -c Release
echo ============================end build=======================================
rem 默认压缩根目录，递归处理子文件夹使用 -r
C:
cd %PUBLISH_DIR%
if not exist %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\temp\ (
	md %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\temp\
)
xcopy %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\release %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\temp\ /s/e/y/exclude:C:\JenkinsPublishSpace\dotnet_exclude.txt

cd %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\temp
WinRAR.exe a -k -r -s -m3 -o+ -ep1 %PROJECT_DIR%.rar
echo publish ok
cd %PUBLISH_DIR%
rem 解压到当前目录下,不包含压缩包内的路径
rem WinRAR.exe e  num_all_tg.zip
rem 解压到制定目录下,不包含压缩包内的路径
rem WinRAR.exe e  num_all_tg.zip .\test_d2

rem for line in `cat tomcat_info.txt`
rem do
rem kill -9 $line
rem done