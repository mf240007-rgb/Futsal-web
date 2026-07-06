@echo off
setlocal
set MAVEN_PROJECTBASEDIR=%~dp0
if exist "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" goto execute
for /f "delims=" %%I in ('cd') do set _cwd=%%I

:execute
set MAVEN_WRAPPER_CMD_LINE_ARGS=%*
set MAVEN_CMD_LINE_ARGS=%MAVEN_WRAPPER_CMD_LINE_ARGS%
set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%
set MAVEN_SKIP_RC=
set MAVEN_SKIP_RC=%MAVEN_SKIP_RC%
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if not exist "%JAVA_EXE%" set JAVA_EXE=java.exe

"%JAVA_EXE%" -cp "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %MAVEN_CMD_LINE_ARGS%
endlocal
