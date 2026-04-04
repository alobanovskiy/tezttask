@ECHO OFF
SETLOCAL

SET "MVNW_DIR=%~dp0"
SET "MVNW_REPO=%MVNW_DIR%.mvn\wrapper"
SET "WRAPPER_JAR=%MVNW_REPO%\maven-wrapper.jar"
SET "PROPS_FILE=%MVNW_REPO%\maven-wrapper.properties"

IF NOT EXIST "%PROPS_FILE%" (
  ECHO Maven wrapper properties not found: "%PROPS_FILE%"
  EXIT /B 1
)

FOR /F "usebackq tokens=1,* delims==" %%A IN ("%PROPS_FILE%") DO (
  IF "%%A"=="wrapperUrl" SET "WRAPPER_URL=%%B"
)

IF "%WRAPPER_URL%"=="" (
  ECHO wrapperUrl not found in "%PROPS_FILE%"
  EXIT /B 1
)

IF NOT EXIST "%WRAPPER_JAR%" (
  ECHO Downloading Maven Wrapper...
  POWERSHELL -NoProfile -ExecutionPolicy Bypass -Command ^
    "$ProgressPreference='SilentlyContinue';" ^
    "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12;" ^
    "New-Item -Force -ItemType Directory -Path '%MVNW_REPO%' | Out-Null;" ^
    "Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%';"
  IF ERRORLEVEL 1 (
    ECHO Failed to download Maven Wrapper JAR from "%WRAPPER_URL%"
    EXIT /B 1
  )
)

WHERE java >NUL 2>&1
IF ERRORLEVEL 1 (
  ECHO Java not found. Please install JDK and add java to PATH.
  EXIT /B 1
)

SET "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.multiModuleProjectDirectory=%MVNW_DIR%"
java %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
EXIT /B %ERRORLEVEL%

