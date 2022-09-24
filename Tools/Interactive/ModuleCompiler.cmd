:: -----------------------------
::   Truncheon Tools Suite 3.1
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
:: Purpose : A tool which helps
:: in compiling the program and
:: the documentation.
::
:: THIS IS NOT RECOMMENDED FOR
::         END USERS!
::
:: =============================
::
:: NOTE: THIS IS A SILENT TOOL!
::   USER INTERACTION IS NOT
::         RECOMMENDED!
::
:: This tool is to be used in a
:: terminal session or in editor
::  terminal sessions for quick
::     and easy compilation
::
:: =============================


:: Set the terminal echo mode to off
@ECHO OFF

:: Clear the screen
CLS

:: Display the build information
ECHO ========================
ECHO     Nion Tools Suite   
ECHO ========================
ECHO VERSION  : 3.1.1
ECHO DATE     : 25-SEPT-2022
ECHO CODENAME : TULZSCHA
ECHO ------------------------

set /p "packageName=Enter the Project Name: "

:: Compile the Program then
ECHO Compiling Truncheon Module Project...
javac -d ../Binaries ./Truncheon/Modules/%packageName%/ModuleRunner.java