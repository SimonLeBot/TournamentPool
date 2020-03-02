@echo off
REM This script should be run once to set the environment variables for the build script.
REM As a prerequisite, ensure the jdk 1.8 located in 3rdParty/Java/jdk  is installed.
REM Usage:
REM   build_cfg.bat

REM ******************************************************************************
REM ******************************************************************************
REM **                                                                          **
REM **  WARNING: if you change this file, the environment variable settings     **
REM **           in the TeamCity builds likely must also be updated.  Tell Ray. **
REM **                                                                          **
REM ******************************************************************************
REM ******************************************************************************

:SetUpEnvironment
echo Setting up Environment for KUtils Build:

:CanopusEnv
if NOT "%THIRD_PARTY_DIR%" == "" goto AntJavaEnv
echo .   Tools, Libs, Canopus, 3rd-party folders and paths.
PUSHD ..\DevTools\3rdParty
if ERRORLEVEL 1 echo ERROR, no 3rd Party Folder
set THIRD_PARTY_DIR=%CD%
echo 3rd. %THIRD_PARTY_DIR%
POPD
PUSHD ..\DevTools\Tools
set TOOLS_DIR=%CD%
if ERRORLEVEL 1 echo ERROR, no Tools Folder
POPD
PUSHD ..\DevTools\..\Libs
set LIBS_DIRECTORY=%CD%
if ERRORLEVEL 1 echo ERROR, no Libs Folder
POPD

:AntJavaEnv
REM check to see if Ant's environment is already set up
echo .   Java and Ant folders.
set ANT_HOME=%THIRD_PARTY_DIR%\ant\apache-ant-1.7.0
if exist "%ProgramFiles%\Java\jdk1.8.0_40" (
    set JAVA_HOME=%ProgramFiles%\Java\jdk1.8.0_40
) else (
    if NOT "%JAVA_JDK_HOME%"=="" (
		set JAVA_HOME=%JAVA_JDK_HOME%
    ) else (	
        echo Error: Can't find Java JDK installation
    )
)
set PATH=%PATH%;%ANT_HOME%\bin;%JAVA_HOME%\bin


:Validate
REM Validate the environment
set ERROR=
if NOT exist "%ANT_HOME%" (
    set ERROR=ANT_HOME=%ANT_HOME% does not exist
) else if NOT exist "%JAVA_HOME%" (
    set ERROR=JAVA_HOME="%JAVA_HOME%" does not exist
)
if NOT "%ERROR%" == "" (
    REM Display error in prompt
    prompt $_ERROR:%ERROR%$_$p$g
    goto endOfScript
)

:PromptTime
REM Adjust the prompt to include the current time
prompt $_(Time:$t) (Date:$d)$_$p$g

:endOfScript
echo done.
