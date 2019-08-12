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
if "%PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\index.html"=="" (
echo fetch code failed
exit 1
)
cd %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%
rem rd /S /Q %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\.git
rem del %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\package-lock.json
rem 拉取代码
rem git checkout develop
rem git pull origin develop

rem dotnet build
rem if exist %PUBLISH_DIR%\%PROJECT_DIR% (
	rem rd /S /Q %PUBLISH_DIR%\%PROJECT_DIR%
rem )
rem md %PUBLISH_DIR%\%PROJECT_DIR%
rem 拷贝源码到指定目录
rem xcopy %PUBLISH_DIR%\gitCode\%PROJECT_DIR% %PUBLISH_DIR%\%PROJECT_DIR%\ /s/e/y
rem 拷贝node_modules到指定目录
WinRAR.exe x -y -o+ %PUBLISH_DIR%\node_modules.rar %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\
rem xcopy %PUBLISH_DIR%\node_modules %PUBLISH_DIR%\%PROJECT_DIR%\node_modules\ /s/e/y
rem cd %PUBLISH_DIR%\%PROJECT_DIR%
echo start npm install……
call npm install --registry=https://registry.npm.taobao.org
echo start npm build……
call npm run build
if not exist %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\dist (
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
copy %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\web.config %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\dist /y
copy %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\Dockerfile %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\dist /y
xcopy %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\WEB-INF %PUBLISH_DIR%\%PROJECTGROUP_DIR%\%PROJECT_DIR%\dist\WEB-INF\ /s/e/y
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