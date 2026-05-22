@echo off
setlocal

set "DIRNAME=%~dp0"
if "%DIRNAME%"=="" set "DIRNAME=."
set "APP_HOME=%DIRNAME%"

set "BUNDLED_MVN=%APP_HOME%apache-maven-3.9.14\bin\mvn.cmd"
if exist "%BUNDLED_MVN%" (
  call "%BUNDLED_MVN%" -f "%APP_HOME%pom.xml" %*
  exit /b %ERRORLEVEL%
)

set "WRAPPER_JAR=%APP_HOME%.mvn\wrapper\maven-wrapper.jar"
if exist "%WRAPPER_JAR%" (
  if "%JAVA_HOME%"=="" (
    set "JAVA_EXE=java.exe"
  ) else (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
  )

  "%JAVA_EXE%" -classpath "%WRAPPER_JAR%" -Dmaven.multiModuleProjectDirectory=%APP_HOME% org.apache.maven.wrapper.MavenWrapperMain %*
  exit /b %ERRORLEVEL%
)

echo No se encontro Maven local ni maven-wrapper.jar.
echo Usa .\apache-maven-3.9.14\bin\mvn.cmd o agrega Maven al PATH.
exit /b 1
