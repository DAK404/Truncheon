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
:: Purpose : A tool which has
:: additional features after the
:: code has been compiled.
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================

:: Set the echo off to stop displaying all command execution on console
@echo off

:: A label to display the menu to the user
:MENU

:: Clear the screen
CLS

:: Display the menu with choices
ECHO :::::::::::::::::::::::::::::
ECHO : TRUNCHEON TOOLS SUITE 3.0 :
ECHO :::::::::::::::::::::::::::::
ECHO.
ECHO  1. START TRUNCHEON
ECHO  2. START DEBUGGER
ECHO  3. CLEAN DIRECTORY
ECHO  4. RESET INSTALLATION
ECHO  5. OPEN COMMAND PROMPT HERE
ECHO  6. OPEN WSL PROMPT HERE
ECHO  7. HELP
ECHO  8. EXIT
ECHO.
ECHO :::::::::::::::::::::::::::::
ECHO.

:: Accept input from user for the desired action
ECHO Please enter your choice [ 1 - 8 ]
choice /N /C 12345678 /M "DBG_TOOLS ->  "

:: Exit if the key pressed is 5
IF errorlevel == 8 GOTO :EOF

:: Go to the HELP label if the key pressed is 4
IF errorlevel == 7 GOTO HELP

IF errorlevel == 6 GOTO OPEN_WSL

IF errorlevel == 5 GOTO OPEN_CMD

IF errorlevel == 4 GOTO RESET_INST

IF errorlevel == 3 GOTO CLEAN_DIR

IF errorlevel == 2 GOTO START_DBGR

IF errorlevel == 1 GOTO START_PRG
