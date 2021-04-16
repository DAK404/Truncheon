@echo off
%SystemRoot%\System32\chcp.com 437 >nul
:MENU
CLS
echo *****************************
echo Truncheon Unified Toolset 2.0
echo *****************************
echo 1. Compile Program
echo 2. Compile Launcher
echo 3. Compile DebugLauncher
echo 4. Compile Documentation
echo 5. Open Command Prompt Here
echo 6. Open Linux Shell Here
echo 7. Open Toolset Help Page
echo 8. Exit Toolset
echo *****************************
echo.
choice /N /C 12345678 /M "Enter an option [1-8] : "
IF errorlevel == 8 GOTO :EOF
IF errorlevel == 7 GOTO HELP
IF errorlevel == 6 GOTO RUN_P2
IF errorlevel == 5 GOTO RUN_P
IF errorlevel == 4 GOTO C_DOC
IF errorlevel == 3 GOTO C_DLNCH
IF errorlevel == 2 GOTO C_LNCH
IF errorlevel == 1 GOTO C_PRG

:C_PRG
javac -d ../Binaries ./Truncheon/Core/Boot.java
ECHO Tip: Compile the Launcher for launching the program.
pause
GOTO MENU

:C_LNCH
javac -d ../Binaries/ ProgramLauncher.java
ECHO Tip: Compile the Program to run the program.
pause
GOTO MENU

:C_DLNCH
javac -d ../Binaries/ DebugLauncher.java
ECHO Tip: Compile the Program to debug the program.
pause
GOTO MENU

:C_DOC
dir /s /B &.java > sources.txt
javadoc -d ../Documentation/Program_Documentation @sources.txt > DocumentationLog.log
del /s /q sources.txt
echo Note: Documentation can be accessed via the "Index.html" in the root of project folder
pause
GOTO MENU

:RUN_P
START cmd.exe
GOTO MENU

:RUN_P2
START wsl
GOTO MENU

:HELP
CLS
ECHO _______________________________
ECHO.
ECHO     Truncheon Toolset Help 
ECHO _______________________________
ECHO.
ECHO.
ECHO HELP DOCUMENT: 1.0
ECHO TIME TO READ : 2 MINUTES
ECHO.
ECHO *******************************
ECHO.
ECHO This tool compiles the program
ECHO Launcher, Debugger and the Java 
ECHO documentation. 
ECHO.
ECHO *******************************
ECHO.
ECHO THE TOOL CONTAINS THE FOLLOWING SECTIONS:
ECHO.
ECHO 1 - COMPILE PROGRAM
ECHO     The tool will run commands to compile the program.
ECHO.
ECHO 2 - COMPILE LAUNCHER
ECHO     The tool will run commands to compile the launcher program only.
ECHO.
ECHO 3 - COMPILE DEBUGLAUNCHER
ECHO     The tool will run commands to compile the debugger.
ECHO.
ECHO 4 - COMPILE DOCUMENTATION
ECHO     The tool will compile the program documentation.
ECHO.
ECHO 5 - OPEN COMMAND PROMPT
ECHO     The tool will start a fresh instance of Windows Command Prompt here.
ECHO.
ECHO 6 - OPEN LINUX SHELL
ECHO     The tool will start a fresh instance of Linux Shell using wsl
ECHO.
ECHO 7 - HELP : WILL DISPLAY THIS HELP SECTION.
ECHO     THE TOOL WILL DISPLAY THIS HELP SECTION.
ECHO.
ECHO 8 - EXIT : EXIT THIS TOOL.
ECHO     EXIT THE TOOL AND RETURN TO WINDOWS.
ECHO.
ECHO *******************************
pause
GOTO MENU