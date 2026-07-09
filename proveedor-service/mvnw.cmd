<# : batch portion
@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET PN=%__MVNW_ARG0_NAME__%
@SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" goto endDetectBaseDir
@SET EXEC_DIR=%CD%
@SET WDIR=%EXEC_DIR%
:findBaseDir
@IF EXIST "%WDIR%"\.mvn goto baseDirFound
@cd ..
@IF "%WDIR%"=="%CD%" goto baseDirNotFound
@SET WDIR=%CD%
@goto findBaseDir
:baseDirFound
@SET MAVEN_PROJECTBASEDIR=%WDIR%
@cd "%EXEC_DIR%"
goto endDetectBaseDir
:baseDirNotFound
@SET MAVEN_PROJECTBASEDIR=%EXEC_DIR%
:endDetectBaseDir
@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
@IF NOT EXIST %WRAPPER_JAR% (@ECHO Descargando Maven Wrapper... & @powershell -Command "Invoke-WebRequest -Uri %DOWNLOAD_URL% -OutFile %WRAPPER_JAR%")
@"%JAVA_HOME%\bin\java.exe" -classpath %WRAPPER_JAR% org.apache.maven.wrapper.MavenWrapperMain %*
