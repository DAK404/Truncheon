@echo off
%SystemRoot%\System32\chcp.com 437 >nul
:MENU
CLS
echo *****************************
echo Truncheon Unified Toolset 2.0
echo *****************************
echo 1. Run Program                
echo 2. Run Debug Build            
echo 3. Run Debugger               
echo 4. Reset Program              
echo 5. Remove source files here    
echo 6. Open Command Prompt Here   
echo 7. Open WSL Shell here        
echo 8. Help                       
echo 9. Exit                       
echo *****************************
ECHO.
choice /N /C 123456789 /M "Enter an option [1-9] : "
IF errorlevel == 9 GOTO :EOF
IF errorlevel == 8 GOTO HELP
IF errorlevel == 7 GOTO RUN_P2
IF errorlevel == 6 GOTO RUN_P
IF errorlevel == 5 GOTO CLEAN_Prog
IF errorlevel == 4 GOTO CLEAN
IF errorlevel == 3 GOTO DEBUG
IF errorlevel == 2 GOTO DEBUG_N
IF errorlevel == 1 GOTO RELEASE

:RELEASE
start "Truncheon: Release Build" java ProgramLauncher Truncheon normal
GOTO MENU

:DEBUG_N
start "Truncheon: Debug Build" java DebugLauncher Truncheon normal
GOTO MENU

:DEBUG
start "Amethyst: Debugger" java DebugLauncher Truncheon debug
GOTO MENU

:CLEAN
@RD /s /q System
@RD /s /q Users
ECHO Cleaned up installation successfully!
pause
GOTO MENU

:CLEAN_Prog
@RD /s /q Truncheon
@RD /s /q *.java
@RD /s /q *.class
ECHO Cleanup complete!
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
ECHO    RunTools.bat Help Section 
ECHO _______________________________
ECHO.
ECHO.
ECHO HELP DOCUMENT: 1.0
ECHO TIME TO READ : 2 MINUTES
ECHO.
ECHO *******************************
ECHO.
ECHO This tool executes the Launcher
ECHO and Debug Launcher Binaries.
ECHO.
ECHO The tool can also help to reset
ECHO the program environment and run
ECHO a command prompt here which can
ECHO help in testing and running the
ECHO functionalities implemented via
ECHO the Command Line/Terminal.
ECHO.
ECHO *******************************
ECHO.
ECHO The following options are implemented in the tool:
ECHO.
ECHO 1 - RUN RELEASE BUILD
ECHO     The tool will run a fresh instance of Truncheon in the "normal" mode.
ECHO.
ECHO 2 - RUN DEBUG BUILD
ECHO     The tool will run a fresh instance of Truncheon in the "debug" mode.
ECHO.
ECHO 3 - RUN DEBUG BUILD (DEBUGGER)
ECHO     The tool will start Truncheon Debugger.
ECHO.
ECHO 4 - RESET PROGRAM
ECHO     Program environment folders such as ".\System" and ".\Users" will be permanently removed.
ECHO.
ECHO 5 - CLEAN DIRECTORY
ECHO     All compiled ".class" and all Java source files ".java" will be removed from the ".\Truncheon" folder.
ECHO.
ECHO 6 - OPEN COMMAND PROMPT HERE
ECHO     The tool will start a fresh instance of Windows Command Prompt here.
ECHO.
ECHO 7 - HELP
ECHO     This option displays this help message!
ECHO.
ECHO 8 - EXIT 
ECHO     Stop this tool and exit to Windows.
ECHO.
ECHO *******************************
pause
GOTO MENU
