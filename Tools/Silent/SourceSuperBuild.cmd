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
:: in compiling everything!
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

:: Display the build information
ECHO ========================
ECHO     Nion Tools Suite   
ECHO ========================
ECHO VERSION : 3.1
ECHO DATE    : 13-MAY-2022
ECHO ------------------------

:: Compile the launcher first
ECHO Compiling Launcher...
javac -d ../Binaries Main.java

:: Compile the Program then
ECHO Compiling Program...
javac -d ../Binaries ./Truncheon/Core/Loader.java

:: List all the files in Source directory and its subdirectories to a sources.txt file
dir /s /B *.java > sources.txt

:: Run the javadoc command to compile the documentation by parsing every file found in sources.txt
:: additionally, write the statuses to the compile log
javadoc -d ../Documentation/JavaDoc -author -version @sources.txt > CompileDoc.log

:: Delete the sources.txt file after use
DEL /s /q sources.txt