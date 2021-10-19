:: -----------------------------
::   Truncheon Tools Suite 3.0
:: -----------------------------
::
:: This tool suite is written to
::  enable developers to easily
::   develop and run Truncheon
::
:: -----------------------------
::
:: =============================
::      Program Information
:: =============================
::
:: Author  : DAK404
:: Purpose : A tool which will
:: help in launching Truncheon
:: with advanced options.
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================

:: Set the echo off to stop displaying all command execution on console
@echo off

:: Initialize variable called BOOT_MODE to store the mode that Truncheon will boot into
SET BOOT_MODE=normal

:: A label to display the menu to the user
:MENU

:: Clear the screen
CLS

:: Display the menu with choices
ECHO :::::::::::::::::::::::::::::
ECHO : TRUNCHEON TOOLS SUITE 3.0 :
ECHO :::::::::::::::::::::::::::::
ECHO.
ECHO  1. RUN IN NORMAL MODE
ECHO  2. RUN IN REPAIR MODE
ECHO  3. RUN MAINTENANCE TASKS
ECHO  4. HELP
ECHO  5. EXIT
ECHO.
ECHO :::::::::::::::::::::::::::::
ECHO.

:: Accept input from user for the desired action
ECHO Please enter your choice [ 1 - 5 ]
choice /N /C 12345 /M "ADV_BOOT ->  "

:: Logic to handle the choices

:: Exit if the key pressed is 5
IF errorlevel == 5 GOTO :EOF

:: Go to the HELP label if the key pressed is 4
IF errorlevel == 4 GOTO HELP

:: > BOOT MODES <
:: --------------
::
:: The choices, 1-3, are about
:: setting Truncheon Boot Modes.
::
:: IF YOU DO NOT KNOW WHAT THIS IS
:: PLEASE DO NOT MODIFY THE CODE BELOW!

:: Go to MAINTAINENCE to boot the program in maintainence mode
IF errorlevel == 3 GOTO MAINTENANCE

:: Go to REPAIR to boot the program in repair mode
IF errorlevel == 2 GOTO REPAIR

:: Go to RUN to run the program normally
IF errorlevel == 1 GOTO RUN

:: Display the help section to the user
:HELP

:: Clear the screen
CLS

:: Display the help section strings
ECHO ___________________________
ECHO. 
ECHO  TRUNCHEON TOOLS SUITE 3.0
ECHO ___________________________
ECHO.
ECHO This tool will help administrators to
ECHO maintain and repair Truncheon. These
ECHO tasks are mainly discussed in the 
ECHO README page. Please refer to it in the
ECHO following link:
ECHO.
ECHO https://dak404.github.io/Truncheon/Readme.html
ECHO. 
ECHO ___________________________
ECHO.
ECHO To choose an option, simply press the desired
ECHO number corresponding to the specified option.
ECHO. 
ECHO 1. RUN IN NORMAL MODE
ECHO This will attempt to launch the program normally.
ECHO. 
ECHO 2. RUN IN REPAIR MODE
ECHO This will boot Truncheon in repair mode.
ECHO. 
ECHO 3. RUN MAINTENANCE TASKS
ECHO This will boot Truncheon in maintenance mode.
ECHO. 
ECHO 4. HELP
ECHO This will display this help section.
ECHO. 
ECHO 5. EXIT
ECHO The tool will quit and exit to Windows.
ECHO. 
ECHO ___________________________
PAUSE
GOTO MENU

:: Set the BOOT_MODE variable to maintenance and run the program
:MAINTENANCE

:: Change the variable value to maintenance
set BOOT_MODE=maintenance

:: Go to RUN to launch the program
GOTO RUN

:: Set the BOOT_MODE variable to repair and run the program
:REPAIR

:: Change the variable value to maintenance
set BOOT_MODE=repair

:: Go to RUN to launch the program
GOTO RUN

:: The logic to run the program with the specified boot mode
:RUN

:: Clear the screen before running the program
CLS

:: Set the JRE path (if it exists) for this session
set PATH=%PATH%;.\JRE\bin

:: Begin the program, and boot it to the specified boot mode
start "Nion Shell 1.0" java ProgramLauncher Truncheon %BOOT_MODE%
