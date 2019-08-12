@echo off
rem dotnet 项目发布
echo ============================begin build=======================================
set PUBLISH_DIR=C:\JenkinsPublishSpace
set "PROJECT_DIR=%1"
if "%PROJECT_DIR%"=="" (
echo This environment prams is needed to run this program
exit 1
)
cd %PUBLISH_DIR%\gitCode\%PROJECT_DIR%
rem dotnet build
rem if exist %PUBLISH_DIR%\%PROJECT_DIR% (
	rem rd /S /Q %PUBLISH_DIR%\%PROJECT_DIR%
rem )
rem md %PUBLISH_DIR%\%PROJECT_DIR%
rem 拷贝源码到指定目录
rem xcopy %PUBLISH_DIR%\gitCode\%PROJECT_DIR% %PUBLISH_DIR%\%PROJECT_DIR%\ /s/e/y
rem 拷贝node_modules到指定目录
WinRAR.exe x -y -o+ %PUBLISH_DIR%\node_modules.rar %PUBLISH_DIR%\gitCode\%PROJECT_DIR%\
rem xcopy %PUBLISH_DIR%\node_modules %PUBLISH_DIR%\%PROJECT_DIR%\node_modules\ /s/e/y
rem cd %PUBLISH_DIR%\%PROJECT_DIR%
call npm install --registry=https://registry.npm.taobao.org
call npm run build
if not exist %PUBLISH_DIR%\gitCode\%PROJECT_DIR%\dist (
echo nodejs build failed
exit 1
)
echo ============================end build=======================================
rem 默认压缩根目录，递归处理子文件夹使用 -r
rem C:
rem cd %PUBLISH_DIR%
rem if not exist %PUBLISH_DIR%\temp\%PROJECT_DIR%\ (
rem 	md %PUBLISH_DIR%\temp\%PROJECT_DIR%\
rem )
rem xcopy %PUBLISH_DIR%\%PROJECT_DIR%\dist %PUBLISH_DIR%\temp\%PROJECT_DIR%\ /s/e/y
rem cd %PUBLISH_DIR%\temp
WinRAR.exe a -k -r -s -m3 -o+ -ep1 %PROJECT_DIR%.rar dist\
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